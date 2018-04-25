'''
构建数据集的短语字典
'''

def getSetKey(filename):
	keySet = set()
	count = 0
	with open(filename, 'r', encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			line = line.split('!')
			for key in line[2:]:
				if key.strip(' ').strip('\n') == "":
					count += 1
					continue
				keySet.add(key.strip(' ').strip('\n'))
	print("bad words: ",count)
	print(len(keySet))
	return keySet

def writeDict(sourceFile,targetFile):
	keySet = getSetKey(sourceFile)
	with open(targetFile,'w',encoding='utf-8') as w:
		for key in keySet:
			w.write(key + " " + str(200) + '\n')

def getDict():
	sourceFile = "E:\\tech_analysis\\py\\model\\paper.dat"
	targetFile = "E:\\tech_analysis\\py\\model\\keyDict.dat"
	writeDict(sourceFile=sourceFile, targetFile=targetFile)
if __name__ == "__main__":
	getDict()


















