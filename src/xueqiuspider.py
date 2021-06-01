import requests
import time
import re
import urllib3
import sys
import random

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
proxies = {}
start_url = 'https://xueqiu.com/service/v5/stock/screener/quote/list?page={}&size=30&order=desc&order_by=percent&exchange=CN&market=CN&type=sha&'
headers = {
			"Host": "xueqiu.com",
			"User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36",
			"Referer": "https://xueqiu.com/hq",
			# "Cookie": "device_id=1bc3f7e62a55294135b256919bad7df4; s=c814xxmy21; bid=55919114a627b9cdf7c2cf6d3b021c7e_km95yxdt; acw_tc=2760820616190675986288295e4abf1a86f8125c58b7e47386a040d5cdea12; Hm_lvt_1db88642e346389874251b5a1eded6e3=1619067601; xq_a_token=4296d3528edb68f8e29363040c82486510a38e74; xqat=4296d3528edb68f8e29363040c82486510a38e74; xq_r_token=bdb802dc27dfb8a683ced0c98ce059de3af61b85; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjY4NzIyNzEzNzcsImlzcyI6InVjIiwiZXhwIjoxNjIxNjU5NjIyLCJjdG0iOjE2MTkwNjc2MjIxNTksImNpZCI6ImQ5ZDBuNEFadXAifQ.mfm1pkInkb6__-yifL7xWMOdV8Sl5PZawnsKnRv1kTVy66FUQ8S2CTbLVN2MacjulkY6hSU_SBVeQhNvrj832TvU9pDPG0xoH4pWPlWCB3MgafEo2fPVrN_lzlwcBlFjLfJKnHfZWCyRnTrqXCreO7iEgMbL3CLj10tNSc3CwfowOW-xzM0vNBoU18YzAV2cnYBEPE9kEAJZPB9NT5cpLqgiyo2LxLPUAgr5_5oZQiGpbA5MN7sxKKuDsbJxRolfIH66QIMHdXlZnTnool1YYoyV6C3juzSp5jWjDMJ0TynIrbxTJHuSzbk4CUZcs2-MaXC6dClOiBO8A2_7s37toQ; xq_is_login=1; u=6872271377; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1619067675"
			"Cookie": "device_id=24700f9f1986800ab4fcc880530dd0ed; s=c511yn1n8b; xq_a_token=123d73a3255f22b507a0fe2767ab714d27709036; xqat=123d73a3255f22b507a0fe2767ab714d27709036; xq_r_token=36ef07d26f68bcdc22515d147e7233c7bbc9b891; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjEzNjc0MTYyNDEsImlzcyI6InVjIiwiZXhwIjoxNjIyNzk1ODk0LCJjdG0iOjE2MjAyMDM4OTQyMTQsImNpZCI6ImQ5ZDBuNEFadXAifQ.Q7ehATLmo0v2_IzX5IW4cN-o1nOdkgUI8G-8aV9S080_on7K1_ZSqTAp1881DxX419CS-ENgNAXREg0io-fZMYXZDw4zC8beQEwk9i3jnHok0AqYy2uMbAiCsm0DH7xMkRsYnwMjnn4_oRAarS9UZ0246Dw42pylfBLPuGgVE6VdpHVrJW8z-Od4TBpoX8CvMEG9pFfxvdRZo4QAVFNUnluItEnr2RqoOu61DOnhN5kAgEq2D26INsfbIwAq8f9qw_bY38vBIS6MzZ21dGCTDuUZlWTTKDzS3u19iQktBVdlo6OPgd0cwd7hPCUwZHp4qb-a379hsXclsIXbZBkb3Q; xq_is_login=1; u=1367416241; acw_tc=2760820116216669613018386e4582fccb348d02432920e614bf2e570895d2; is_overseas=0; Hm_lvt_1db88642e346389874251b5a1eded6e3=1620203899,1621666964; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1621666964"
}

def save_file(content_list):
	for content in content_list:
		with open(file_name, 'a')as f:
			f.write(str(content['comment']).encode("gbk", 'ignore').decode("gbk", "ignore"))
			f.write("\n")
			
csv_delim = '`'

def parse_comment_url(url):
	r = requests.get(url, headers=headers, proxies=proxies, verify=False)
	res_list = r.json()['list']  
	count = r.json()['count']
	content_list = []
	for res in res_list:
		item = {}
		#item['user_name'] = res['user']['screen_name']
		item['comment'] = res['text']
		item['comment'] = re.sub("<.*?>||&nbsp;||\`",'', item['comment'])
		item['comment'] = item['comment'] + csv_delim
		content_list.append(item)
	return content_list, count

# Parameters for random labels
label_cat_cnt_L = 1
label_cat_cnt_R = 16 + 1
label_cnt_L = 1
label_cnt_R = 8 + 1
random_char_L = ord('A')
random_char_R = ord('Z') + 1
label_cat_len_without_suffix_L = 1
label_cat_len_without_suffix_R = 1 + 1
label_len_without_suffix_L = 2
label_len_without_suffix_R = 2 + 1
name_dsuffix_L = 1
name_dsuffix_R = 64 + 1

def random_char(L, R):
	return chr(int(random.uniform(L, R)))

def random_uniform_int(L, R):
	return int(random.uniform(L, R))

def parse_comment_url_with_random_test_labels(url):
	r = requests.get(url, headers=headers, proxies=proxies, verify=False)  
	res_list = r.json()['list']  
	count = r.json()['count']
	content_list = []
	# 处理每条股评
	for res in res_list:
		item = {}
		#item['user_name'] = res['user']['screen_name']
		str_parts = []

		# 添加股评
		str_parts.append(re.sub("<.*?>||&nbsp;||\`",'', res['text'])) # 最左一列为股评（已消去特殊字符）
		str_parts.append(csv_delim)

		# 构造标注数据，测试用
		label_cat_cnt = random_uniform_int(label_cat_cnt_L, label_cat_cnt_R)
		for i in range(label_cat_cnt): # 随机构造若干类标签
			# 随机生成标签类名称
			label_cat_len = random_uniform_int(label_cat_len_without_suffix_L, label_cat_len_without_suffix_R)
			for j in range(label_cat_len): # 随机生成标签类名称的每一个字符
				str_parts.append(random_char(random_char_L, random_char_R))
			str_parts.append(str(random_uniform_int(name_dsuffix_L, name_dsuffix_R))) # 为标签类名称随机添加一个数字后缀

			# 随机在该标签类下构造若干个标签
			label_cnt = random_uniform_int(label_cnt_L, label_cnt_R)
			for j in range(label_cnt):
				str_parts.append(' ')
				label_len_without_suffix = random_uniform_int(label_len_without_suffix_L, label_len_without_suffix_R)
				for k in range(label_len_without_suffix): # 随机生成标签的每一个字符
					str_parts.append(random_char(random_char_L, random_char_R))
				str_parts.append(str(random_uniform_int(name_dsuffix_L, name_dsuffix_R))) # 为标签名称随机添加一个数字后缀
			str_parts.append(csv_delim)

		# 最终构造出一行字符串并添加到股评列表
		# print("str_parts = \n")
		# print(str_parts)
		item['comment'] = ''.join(str_parts)
		content_list.append(item)
	return content_list, count

# Parameters for crawling
access_delay = 3
reaccess_delay = 5
crawl_pages = 1

file_name = sys.argv[1] # 保存的文件名
stock_num = sys.argv[2] # 输入股票代码
print(sys.argv)
for i in range(crawl_pages):
	detail_url = "https://xueqiu.com/statuses/search.json?count=10&comment=0&symbol={}&hl=0&source=all&sort=&page={}&q=&type=11".format(stock_num, i + 1)
	try:
		if ("-r" in sys.argv or "--random" in sys.argv):
			print("正爬取：" + detail_url + " 并生成测试用的随机标注。")
			comment_data, count = parse_comment_url_with_random_test_labels(detail_url)
		else:
			print("正爬取：" + detail_url)
			comment_data, count = parse_comment_url(detail_url)
		time.sleep(access_delay)
	except Exception as e: 
		print("Error: ", e)
		time.sleep(reaccess_delay)
		if ("-r" in sys.argv or "--random" in sys.argv):
			print("正爬取：" + detail_url + " 并生成测试用的随机标注。")
			comment_data, count = parse_comment_url_with_random_test_labels(detail_url)
		else:
			print("正爬取：" + detail_url)
			comment_data, count = parse_comment_url(detail_url)
		time.sleep(reaccess_delay)
	save_file(comment_data)