
## Python Mapred示例

<p>"wordcount" 词频统计，运行命令：cat aw.log| python2 mapper.py| sort -k1 | python2 reducer.py</p>
<p>"applog" 数据表关联，运行命令： cat data.txt |python2 mapper.py | sort |python2 reducer.py</p>
<p>"matrix" 矩阵计算，运行命令：cat data.txt |python2 mapper.py |sort -t$'\t'|python2 reducer.py</p>
