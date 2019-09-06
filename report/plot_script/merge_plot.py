#!/usr/bin/env python
import matplotlib.pyplot as plt
import re 
def pattern_ylabel(y):
    """
    pattern ylabel
    """
    pattern_num=str(y)
    if y/10000 >= 1:
        pattern=r'(\d+)(\d{4})((,\d{4})*)'
    else:
        pattern=r'(\d+)(\d{3})((,\d{3})*)'
    while True:
        pattern_num,count=re.subn(pattern,r'\1,\2\3',pattern_num)
        if count==0:
            break
    return re.split(re.compile(r','),pattern_num)[0]+'k'

def perf_data(v3,v4,dataset):
    """
    generate dataset for plot
    """
    with open(v3) as file_v3:
       i = 1
       for line in file_v3:
           items = line.split(" ")
           dataset['build_num'].append(int(i))
           if items[6][:-2] == '' or '*' in items[6]:
               dataset['3.0_version'].append(0)
           else:
               dataset['3.0_version'].append(float(items[6][:-2]))
           i = i+1
    with open(v4) as file_v4:
       i = 1
       for line in file_v4:
           items = line.split(" ")
           if items[6][:-2] == '' or '*' in items[6]:
               dataset['4.0_version'].append(0)
           else:
               dataset['4.0_version'].append(float(items[6][:-2]))
           i = i+1
        

def perf_line_plots(v3,v4,title,pname):
    """
    make plot 
    """    
    dataset = {
       'build_num': [],
       '3.0_version': [],
       '4.0_version': []
    }
    perf_data(v3,v4,dataset)
    plt.title(title)
    plt.plot(dataset['build_num'], dataset['3.0_version'], color='b', label='3.0version')
    for i in range(len(dataset['build_num'])):
        plt.text(dataset['build_num'][i],dataset['3.0_version'][i],pattern_ylabel(dataset['3.0_version'][i]))
    
    plt.plot(dataset['build_num'], dataset['4.0_version'], color='cyan', label='4.0version')
    for i in range(len(dataset['build_num'])):
        plt.text(dataset['build_num'][i],dataset['4.0_version'][i],pattern_ylabel(dataset['4.0_version'][i]))
    plt.xlim() 
    plt.legend()
    plt.xlabel('build_num')
    plt.ylabel('throught(/s)')
    plt.xticks(range(10))
    plt.savefig(pname)
    plt.show()  
  
if __name__=='__main__':
   perf_line_plots('./plot/3.0_single_insert_merge.log_plot','./plot/4.0_single_insert_merge.log_plot','insert throught','insert_test.png')
