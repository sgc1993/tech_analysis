# -*- coding:utf-8 -*-
import BuildDict
import CutItemWithKey
import CutItemWithKey
'''
加载预料、训练模型，写下模型，将向量和词写入Word2Vec.dat文件
'''
import sys
# 引入 word2vec
from gensim.models import word2vec
import gensim
# 引入日志配置
import logging
import numpy as np

# logging.basicConfig(format='%(asctime)s : %(levelname)s : %(message)s', level=logging.INFO)
#
# # 引入数据集
# raw_sentences = ["the quick brown fox jumps over the lazy dogs","yoyoyo you go home now to sleep"]
#
# # 切分词汇
# sentences= [s.split(' ') for s in raw_sentences]
# print(sentences)
#
# # 构建模型
# model = word2vec.Word2Vec(sentences, min_count=1)
#
# # 进行相关性比较
# print(model.similarity('dogs','you'))
#
# # model = gensim.models.Word2Vec(iter=1)  # an empty model, no training yet
# # model.build_vocab(some_sentences)  # can be a non-repeatable, 1-pass generator
# # model.train(other_sentences)  # can be a non-repeatable, 1-pass generator

def getSentences(filename):
	sentences = []
	i = 0
	with open(filename,'r',encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			if line.strip(' ').strip('\n') == "":
				continue
			# i += 1
			# assert i < 10
			temp = [key for key in line.split(',') if key.strip(' ').split('\n') != ""]
			# print(temp)
			sentences.append(temp)
		print(len(sentences))
	return sentences

def buildModelAndTrainAndSave():
	filename = 'E:\\tech_analysis\\py\\model\\DealedData.dat'
	sentences = getSentences(filename)
	print("training model.....")
	# model = word2vec.Word2Vec(sentences=sentences,size=200,iter=50,max_vocab_size=50000,compute_loss=True)
	model = word2vec.Word2Vec(sentences=sentences,size=200,iter=50,min_count=2,max_vocab_size=50000,compute_loss=True)

	# model = gensim.models.Word2Vec(iter=10)
	# model.build_vocab(sentences)
	# model.train(sentences)
	model.save('E:\\tech_analysis\\py\\model\\dealedData.model')
	print("training end")
	# model.save('dealedData.model')
	# model.save('dealedData1.model')
	# model.save_word2vec_format('text.model.bin', binary=True)

def loadModel():
	model = word2vec.Word2Vec.load('E:\\tech_analysis\\py\\model\\dealedData.model')
	# model = word2vec.Word2Vec.load('dealedData1.model')
	return model
	# model1 = word2vec.Word2Vec.load_word2vec_format('text.model.bin', binary=True)


def predict(model,word):
	res = model.most_similar([word],topn=5)
	# ans = [key[0].encode(encoding='GBK') for key in res]
	# ans = [key[0].encode(encoding='UTF-8') for key in res]
	ans = [key[0] for key in res]
	return ans


def getVec(model,word):
	print(model[word])  # raw NumPy vector of a word
# array([-0.00449447, -0.00310097,  0.02421786, ...], dtype=float32)

def getUniqueWord(filename):
	keyWords = set()
	i = 0
	with open(filename, 'r', encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			if line.strip(' ').strip('\n') == "":
				continue
			# print(line)
			temp = [key for key in line.split(',') if key.strip(' ').split('\n') != ""]
			# print(len(temp))
			for key in temp:
				if '.' in key or 'cm' in key or 'nm' in key:
					continue
				keyWords.add(key)
			# print(len(keyWords))
			# print(keyWords)
			# i += 1
			# assert i < 10
	print(len(keyWords))
	return keyWords
def writeWordAndVector(model, targetFilename):
	count = 0
	with open(targetFilename,'w',encoding='utf-8') as w:
		for key in model.wv.vocab:
			count += 1
			# print(count)
			w.write(key+",")
			for f in model[key].astype(dtype=np.float64):
				w.write(str(f)+',')
			w.write('\n')
	print("cccccccccccccccccccccc",count)
	print('Done')

def test():
	# filename = 'DealedData.dat'
	# uniqueWord = getUniqueWord(filename)
	# print(len(uniqueWord))

	# print(sys.argv[1])
	# buildModelAndTrainAndSave()
	model = loadModel()

	targetFilename = 'E:\\tech_analysis\\py\\model\\Word2Vec.dat'
	writeWordAndVector(model, targetFilename)

	# model = word2vec.Word2Vec.load('dealedData.model')
	# print("ending........")
	print(model.get_latest_training_loss())
	print(len(model.wv.vocab))
	print(type(model.wv.vocab))

	# count = 0
	# for key in model.wv.vocab:
	# 	count += 1
	# 	print(key)
	# print(count)

	# print(len(model.vocabulary))
	# for key in model.vocabulary:
	# 	print(key)
	# print(model['正电子寿命'])
	# print(model['能源管理'])
	# print(predict(model,sys.argv[1]))
	print(predict(model, '卫星通信'))

	# print(predict(model,'能源管理'))
	# for key in predict(model,'卫星通信'):
	# 	print(key.decode(encoding='GBK'))
	# 	print(key.decode(encoding='UTF-8'))
	# print(predict(model,'人工智能'))
	# getVec(model,'卫星通信')
def forJava():
	print("Starting deal data........")
	buildModelAndTrainAndSave()
	model = loadModel()
	targetFilename = 'E:\\tech_analysis\\py\\model\\Word2Vec.dat'
	writeWordAndVector(model, targetFilename)
	print("End....")
	print("the file model needed done")

def getData():
	BuildDict.getDict()
	CutItemWithKey.getDealData()

if __name__ =='__main__':
	getData()
	forJava()


