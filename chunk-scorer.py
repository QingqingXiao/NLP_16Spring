#!/usr/bin/python
#
# scorer for NLP class Spring 2016
# ver.1.0
#
# score a key file against a response file
# both should consist of lines of the form:   token \t tag
# sentences are separated by empty lines
#
def score (keyFileName, responseFileName):
	keyFile = open(keyFileName, 'r')
	key = keyFile.readlines()
	responseFile = open(responseFileName, 'r')
	response = responseFile.readlines()
	if len(key) != len(response):
    		print "length mismatch between key and submitted file"
		exit()
	correct = 0
	incorrect = 0
	keyGroupCount = 0
	keyStart = 0
	responseGroupCount = 0
	responseStart = 0
	correctGroupCount = 0
	for i in range(len(key)):
		key[i] = key[i].rstrip('\n')
		response[i] = response[i].rstrip('\n')
		if key[i] == "":
			if response[i] == "":
				continue
			else:
    				print "sentence break expected at line " + str(i)
				exit()
    		keyFields = key[i].split('\t')
		if len(keyFields) != 2:
    			print "format error in key at line " + str(i) + ":" + key[i]
			exit()
		keyToken = keyFields[0]
		keyTag = keyFields[1][0:1]
    		responseFields = response[i].split('\t')
		if len(responseFields) != 2:
    			print "format error at line " + str(i)
			exit()
		responseToken = responseFields[0]
		responseTag = responseFields[1][0:1]
    		if responseToken != keyToken:
    			print "token mismatch at line " + str(i)
			exit()
		if responseTag == keyTag:
			correct = correct + 1
		else:
			incorrect = incorrect + 1
		responseEnd =  responseStart != 0 and (responseTag=='O' or responseTag=='B')
		responseBegin = responseTag == 'B' or (responseStart==0 and responseTag=='I')
		keyBegin = keyTag == 'B' or (keyStart==0 and keyTag=='I')
		keyEnd =  keyStart != 0 and (keyTag=='O' or keyTag=='B')
		if responseEnd: 
		    responseGroupCount = responseGroupCount + 1
		if keyEnd:
		    keyGroupCount = keyGroupCount + 1
		if responseEnd and keyEnd and responseStart == keyStart:
		    correctGroupCount = correctGroupCount + 1
		if responseBegin:
		    responseStart = i
		elif responseEnd:
		    responseStart = 0
		if keyBegin:
		    keyStart = i
		elif keyEnd:
		    keyStart = 0
	print correct, "out of", str(correct + incorrect) + " tags correct"
	accuracy = 100.0 * correct / (correct + incorrect)
	print "  accuracy: %5.2f" % accuracy
	print keyGroupCount, "groups in key"
	print responseGroupCount, "groups in response"
	print correctGroupCount, "correct groups"
	precision = 100.0 * correctGroupCount / responseGroupCount
	recall = 100.0 * correctGroupCount / keyGroupCount
	F = 2 * precision  * recall / (precision + recall)
	print "  precision: %5.2f" % precision
	print "  recall:    %5.2f" % recall
	print "  F1:        %5.2f" % F
score ('data/WSJ_24.chunk', 'data/response.chunk')