import jieba
'''
使用结巴对数据进行处理，构建训练语料
'''
# jieba.load_userdict("keydict.dat")
# str_text = "基于打靶和遗传算法模拟多阶级联拉曼光纤激光器。" \
# 		   "直接采用打靶算法数值求解表征光纤中受激拉曼散射效应的非线性耦合方程组对多阶段联拉曼光纤激光" \
# 		   "器的输出特性进行仿真,任意设置的各阶Stokes光功率初始值会导致计算过程不收敛.本文提出首先采用遗" \
# 		   "传算法对各阶的Stokes光功率初值进行优化筛选,解决了各阶Stokes光功率初值选择的收敛性问题,然后利" \
# 		   "用四阶龙格-库塔及打靶算法进行数值求解,结果表明该方法非常有效.在此基础上,对六阶掺锗级联拉曼光" \
# 		   "纤激光器进行了数值模拟,并比较分析了光纤长度、输出耦合器反射率、泵浦功率等因素对六阶级联拉曼" \
# 		   "光纤激光器输出特性的影响."
#
# str_load = jieba.cut(str_text)
# cutList = [key for key in str_load ]
# print(cutList)

def writeCutKeyword(sourceFile,targetFile,stopwordsFile):
	stopwords = []
	badStopCount = 0
	with open(stopwordsFile,'r',encoding='utf-8') as r:
		lines = r.readlines()
		for line in lines:
			if line.strip(' ').split('\n') == "":
				badStopCount += 1
				continue
			stopwords.append(line.strip('\n'))
	print(len(stopwords))
	# print(stopwords)
	print("badStopCount:",badStopCount)
	i= 0
	with open(sourceFile,'r',encoding='utf-8') as r:
		lines = r.readlines()
		with open(targetFile,'w',encoding='utf-8') as w:
			for line in lines:
				# i += 1
				# assert i < 10
				str_load = jieba.cut(line)
				cutList = [key.strip(' ').strip('\n') for key in str_load
						   if key.strip(' ').strip('\n') not in stopwords and len(key.strip(' ').strip('\n')) > 2]
				# print(cutList)
				for key in cutList:
					w.write(key+',')
				w.write('\n')
				# w.writelines(cutList+"\n")
def getDealData():
	jieba.load_userdict("E:\\tech_analysis\\py\\model\\keydict.dat")
	sourceFile = "E:\\tech_analysis\\py\\model\\OriginalData.dat"
	targetFile = "E:\\tech_analysis\\py\\model\\DealedData.dat"
	stopwordsFile = "E:\\tech_analysis\\py\\model\\stopwords.txt"
	writeCutKeyword(sourceFile, targetFile, stopwordsFile)
if __name__ =="__main__":
	getDealData()
