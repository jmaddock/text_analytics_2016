import os
import pandas as pd
import numpy as np
import argparse

NEG_DATA_DIR = os.path.join(os.pardir,'text/review_polarity/txt_sentoken/neg')
POS_DATA_DIR = os.path.join(os.pardir,'text/review_polarity/txt_sentoken/pos')
TRAINING_FILES = ['cv00']
TEST_FILES = ['cv60']

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--train',nargs='+')
    parser.add_argument('--test',nargs='+')
    parser.add_argument('--output')
    args = parser.parse_args()
    TRAINING_FILES = args.train
    TEST_FILES = args.test
    
    classList = {'neg':{},'pos':{}}
    classList['neg']['dirPath'] = NEG_DATA_DIR
    classList['pos']['dirPath'] = POS_DATA_DIR

    # train classifier
    # for each class (pos and neg)
    for x in classList:
        trainingFileList = []
        # create a list of files based on the file name prefix
        for trainingFilePrefix in TRAINING_FILES:
            for fileName in os.listdir(classList[x]['dirPath']):
                if trainingFilePrefix in fileName:
                    trainingFileList.append(fileName)

        # for each file in fileList
        # split into list of words on \n and " "
        # add each word to df
        vocab = pd.DataFrame({'word':[]})
        print('training on file...')
        for fileName in trainingFileList:
            print(fileName)
            f = open(os.path.join(classList[x]['dirPath'],fileName),"r")
            for line in f:
                wordList = line.split()
                for word in wordList:
                    vocab = vocab.append(pd.DataFrame([{'word':word}]))

        # get word counts for each class
        vocabFrequency = vocab.groupby(['word']).size().to_frame('count').reset_index()
        # Laplace smoothing
        vocabFrequency['count'] = vocabFrequency['count'] + 1
        # get the total number of words for each class
        total = vocabFrequency['count'].sum()
        # normalize the frequencies
        vocabFrequency['count'] = vocabFrequency['count'].divide(total)
        # get log probability
        vocabFrequency['count'] = vocabFrequency['count'].apply(np.log)
        print(vocabFrequency)
        classList[x]['vocabFrequency'] = vocabFrequency

    results = pd.DataFrame({'filename':[],'class':[],'nb_class':[]})
    if args.output:
        outFile = open(args.output,'a')
    for x in classList:
        testFileList = []
        # create a list of files based on the file name prefix
        for testFilePrefix in TEST_FILES:
            for fileName in os.listdir(classList[x]['dirPath']):
                if testFilePrefix in fileName:
                    testFileList.append(fileName)
        print('classifying file...')
        # for each test file
        for fileName in testFileList:
            print(fileName)
            f = open(os.path.join(classList[x]['dirPath'],fileName),"r")
            # set probabilities for each class to 1
            negProb = 1
            posProb = 1
            # split file into lines, lines into words
            for line in f:
                wordList = line.split()
                for word in wordList:
                    # try to find to find the probability that the word belongs to a class
                    # if it doesn't exist in the vocabulary, add 0
                    try:
                        negProb += classList['neg']['vocabFrequency'].loc[classList['neg']['vocabFrequency']['word'] == word]['count'].iloc[0]
                    except IndexError as e:
                        pass
                    try:
                        posProb += classList['pos']['vocabFrequency'].loc[classList['pos']['vocabFrequency']['word'] == word]['count'].iloc[0]
                    except IndexError as e:
                        pass
            print (negProb, posProb)
            # classify as the higher probability
            if negProb > posProb:
                results = results.append(pd.DataFrame([{'filename':fileName,'class':x,'nb_class':'neg'}]))
            else:
                results = results.append(pd.DataFrame([{'filename':fileName,'class':x,'nb_class':'pos'}]))
    print(results)
    # calculate number of true positives
    tp = len(results.loc[(results['class'] == 'pos') & (results['nb_class'] == 'pos')])
    # calculate number of false positives
    fp = len(results.loc[(results['class'] == 'neg') & (results['nb_class'] == 'pos')])
    # calculate number of false negatives
    fn = len(results.loc[(results['class'] == 'pos') & (results['nb_class'] == 'neg')])
    print(results.loc[(results['nb_class'] == 'pos')])
    print(results.loc[(results['nb_class'] == 'neg')])
    # calculate precision
    precision = tp/(tp + fp)
    # calculate recall
    recall = tp/(tp + fn)
    # calculate fscore
    fscore = (2 * precision * recall)/(precision + recall)
    resultOutput = 'precision: {0}, recall: {1}, fscore: {2}\n'.format(precision,recall,fscore)
    print(resultOutput)
    if args.output:
        outFile.write('training files: {0} ({1} files)\n'.format(TRAINING_FILES,len(trainingFileList)))
        outFile.write('test files: {0} ({1} files)\n'.format(TEST_FILES,len(testFileList)))
        outFile.write(resultOutput)
    
if __name__ == "__main__":
    main()

