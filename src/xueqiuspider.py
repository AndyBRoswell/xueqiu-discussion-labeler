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
			"Encoding": "GBK",
			# "Cookie": "device_id=1bc3f7e62a55294135b256919bad7df4; s=c814xxmy21; bid=55919114a627b9cdf7c2cf6d3b021c7e_km95yxdt; acw_tc=2760820616190675986288295e4abf1a86f8125c58b7e47386a040d5cdea12; Hm_lvt_1db88642e346389874251b5a1eded6e3=1619067601; xq_a_token=4296d3528edb68f8e29363040c82486510a38e74; xqat=4296d3528edb68f8e29363040c82486510a38e74; xq_r_token=bdb802dc27dfb8a683ced0c98ce059de3af61b85; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjY4NzIyNzEzNzcsImlzcyI6InVjIiwiZXhwIjoxNjIxNjU5NjIyLCJjdG0iOjE2MTkwNjc2MjIxNTksImNpZCI6ImQ5ZDBuNEFadXAifQ.mfm1pkInkb6__-yifL7xWMOdV8Sl5PZawnsKnRv1kTVy66FUQ8S2CTbLVN2MacjulkY6hSU_SBVeQhNvrj832TvU9pDPG0xoH4pWPlWCB3MgafEo2fPVrN_lzlwcBlFjLfJKnHfZWCyRnTrqXCreO7iEgMbL3CLj10tNSc3CwfowOW-xzM0vNBoU18YzAV2cnYBEPE9kEAJZPB9NT5cpLqgiyo2LxLPUAgr5_5oZQiGpbA5MN7sxKKuDsbJxRolfIH66QIMHdXlZnTnool1YYoyV6C3juzSp5jWjDMJ0TynIrbxTJHuSzbk4CUZcs2-MaXC6dClOiBO8A2_7s37toQ; xq_is_login=1; u=6872271377; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1619067675"
			# "Cookie": "device_id=24700f9f1986800ab4fcc880530dd0ed; s=c511yn1n8b; xq_a_token=123d73a3255f22b507a0fe2767ab714d27709036; xqat=123d73a3255f22b507a0fe2767ab714d27709036; xq_r_token=36ef07d26f68bcdc22515d147e7233c7bbc9b891; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjEzNjc0MTYyNDEsImlzcyI6InVjIiwiZXhwIjoxNjIyNzk1ODk0LCJjdG0iOjE2MjAyMDM4OTQyMTQsImNpZCI6ImQ5ZDBuNEFadXAifQ.Q7ehATLmo0v2_IzX5IW4cN-o1nOdkgUI8G-8aV9S080_on7K1_ZSqTAp1881DxX419CS-ENgNAXREg0io-fZMYXZDw4zC8beQEwk9i3jnHok0AqYy2uMbAiCsm0DH7xMkRsYnwMjnn4_oRAarS9UZ0246Dw42pylfBLPuGgVE6VdpHVrJW8z-Od4TBpoX8CvMEG9pFfxvdRZo4QAVFNUnluItEnr2RqoOu61DOnhN5kAgEq2D26INsfbIwAq8f9qw_bY38vBIS6MzZ21dGCTDuUZlWTTKDzS3u19iQktBVdlo6OPgd0cwd7hPCUwZHp4qb-a379hsXclsIXbZBkb3Q; xq_is_login=1; u=1367416241; acw_tc=2760820116216669613018386e4582fccb348d02432920e614bf2e570895d2; is_overseas=0; Hm_lvt_1db88642e346389874251b5a1eded6e3=1620203899,1621666964; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1621666964"
			# "Cookie": "device_id=24700f9f1986800ab4fcc880530dd0ed; s=c511yn1n8b; acw_tc=2760820816228937868421541e3f5749ce5aec8979da60b3c8719be2aba40c; is_overseas=0; Hm_lvt_1db88642e346389874251b5a1eded6e3=1621666964,1621826340,1622893795; xq_a_token=c68b6ee9af2393c44a5d9745eb2857bb6943cef6; xqat=c68b6ee9af2393c44a5d9745eb2857bb6943cef6; xq_r_token=cbfa462352b70532e0a62c9cf3d211cb6569f348; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjEzNjc0MTYyNDEsImlzcyI6InVjIiwiZXhwIjoxNjI1NDg1ODAyLCJjdG0iOjE2MjI4OTM4MDIzNjksImNpZCI6ImQ5ZDBuNEFadXAifQ.jJorr34P0Qm0FArk9vBVvr0u6Xj7hTQdXzlc_ZynM4tlipCS1qCtVwWlEEr13DJ-_3sLgOirBsf9qbCZDj8NggHbSjp8xhtpXOOZbe8Vx1j0bX2XZS_6DjmUEHJ_KWQYoKoPOGRptph0hgQowUVMKlzbnQinJBZmklUkXANXR3pxzHNRolcCYKCx7LHVVEWnpP-HyDxhwTMxbtdmkPwY3ivffufc04UnZ6FgkfV4vhw2PkdIgrvYOzMUWHn21kmosSaVY4L1vIiau8t8l3bJtynMpHaiW-YJY175nHpCt_VQ8EM3g28uZFI8gH81OOrHnv_4yz22XunLDX6bV5jKtA; xq_is_login=1; u=1367416241; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1622893810; snbim_minify=true"
			# "Cookie": "Hm_lvt_1db88642e346389874251b5a1eded6e3=1622879531; device_id=24700f9f1986800ab4fcc880530dd0ed; s=cu12f28pr7; bid=d360692e02eca5f65721540af3850bb8_km5upvbt; __utma=1.881906705.1615526421.1615560584.1619190910.4; __utmz=1.1615527147.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); Hm_lpvt_1db88642e346389874251b5a1eded6e3=1625112949; acw_tc=2760820216251128323658231ead1d9c74c3c01de9ec3806367e7e5d2a0ced; xq_a_token=1ca9c6a2567716451c6c72e6d8c1f94141a6a4a5; xqat=1ca9c6a2567716451c6c72e???J1aWQiOjk2NDE5MTEwNzUsImlzcyI6InVjIiwiZXhwIjoxNjI3MzkyNjIxLCJjdG0iOjE2MjUxMTI5MjczMDcsImNpZCI6ImQ5ZDBuNEFadXAifQ.KKchyomhWIntaPZNyIP3NeOoXwJxbDpcMS7oonhtKQ-aBrTmLwSuyrPNZaPAexSPf8ghCDNilAMwQ2pFvG4TrvbubMfc7DBlZW3CobTVYOwAUaeQ4QneykW1UbTl0PKpvsbgWpF6bnyiWYfywc52GYEQVpd_kpHKilssQKwqeKj6nc_sTOFQqacgANpc0qJC7s8e9npn7mvsiwqOsIV_yvFOt2YtKnUx_1RFtN9u0vDot_0svHiE0Kw0hX3dTJPwHdUQ0Mibqfh0Dmdge0gdKZzPHtWqRjF1tKlzN6K_zM7-83Gx_b6j47vgNzSIEpYJd9HGt3t2Mdt6mT-KD2Lv5g; u=9641911075; remember=1; xq_is_login=1; is_overseas=1"
			# "Cookie": "Hm_lvt_1db88642e346389874251b5a1eded6e3=1622879531; device_id=24700f9f1986800ab4fcc880530dd0ed; s=cu12f28pr7; bid=d360692e02eca5f65721540af3850bb8_km5upvbt; __utma=1.881906705.1615526421.1615560584.1619190910.4; __utmz=1.1615527147.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); Hm_lpvt_1db88642e346389874251b5a1eded6e3=1625117035; xq_a_token=1ca9c6a2567716451c6c72e6d8c1f94141a6a4a5; xqat=1ca9c6a2567716451c6c72e6d8c1f94141a6a4a5; xq_r_token=a5d9078efc1b81ac77d0d3339d79169b6cba72a4;???jUxMTI5MjczMDcsImNpZCI6ImQ5ZDBuNEFadXAifQ.KKchyomhWIntaPZNyIP3NeOoXwJxbDpcMS7oonhtKQ-aBrTmLwSuyrPNZaPAexSPf8ghCDNilAMwQ2pFvG4TrvbubMfc7DBlZW3CobTVYOwAUaeQ4QneykW1UbTl0PKpvsbgWpF6bnyiWYfywc52GYEQVpd_kpHKilssQKwqeKj6nc_sTOFQqacgANpc0qJC7s8e9npn7mvsiwqOsIV_yvFOt2YtKnUx_1RFtN9u0vDot_0svHiE0Kw0hX3dTJPwHdUQ0Mibqfh0Dmdge0gdKZzPHtWqRjF1tKlzN6K_zM7-83Gx_b6j47vgNzSIEpYJd9HGt3t2Mdt6mT-KD2Lv5g; u=9641911075; remember=1; xq_is_login=1; acw_tc=2760820816251165186124829e502422068ca35b1bbfbadfbc9694629a0768; is_overseas=0"
			# "Cookie": "Hm_lvt_1db88642e346389874251b5a1eded6e3=1622879531; device_id=24700f9f1986800ab4fcc880530dd0ed; s=cu12f28pr7; bid=d360692e02eca5f65721540af3850bb8_km5upvbt; __utma=1.881906705.1615526421.1615560584.1619190910.4; __utmz=1.1615527147.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); Hm_lpvt_1db88642e346389874251b5a1eded6e3=1625117051; xq_a_token=1ca9c6a2567716451c6c72e6d8c1f94141a6a4a5; xqat=1ca9c6a2567716451c6c72e6d8c1f94141a6a4a5; xq_r_token=a5d9078efc1b81ac77d0d3339d79169b6cba72a4; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjk2NDE5MTEwNzUsImlzcyI6InVjIiwiZXhwIjoxNjI3MzkyNjIxLCJjdG0iOjE2MjUxMTI5MjczMDcsImNpZCI6ImQ5ZDBuNEFadXAifQ.KKchyomhWIntaPZNyIP3NeOoXwJxbDpcMS7oonhtKQ-aBrTmLwSuyrPNZaPAexSPf8ghCDNilAMwQ2pFvG4TrvbubMfc7DBlZW3CobTVYOwAUaeQ4QneykW1UbTl0PKpvsbgWpF6bnyiWYfywc52GYEQVpd_kpHKilssQKwqeKj6nc_sTOFQqacgANpc0qJC7s8e9npn7mvsiwqOsIV_yvFOt2YtKnUx_1RFtN9u0vDot_0svHiE0Kw0hX3dTJPwHdUQ0Mibqfh0Dmdge0gdKZzPHtWqRjF1tKlzN6K_zM7-83Gx_b6j47vgNzSIEpYJd9HGt3t2Mdt6mT-KD2Lv5g; u=9641911075; remember=1; xq_is_login=1; acw_tc=2760820816251165186124829e502422068ca35b1bbfbadfbc9694629a0768"
}

def save_file(content_list, encoding = "GBK"):
	for content in content_list:
		with open(file_name, 'a')as f:
			f.write(str(content['comment']).encode(encoding, 'ignore').decode(encoding, "ignore"))
			f.write("\n")
			
csv_delim = '`'

def parse_comment_url(url):
	r = requests.get(url, headers=headers, proxies=proxies, verify=False)
	res_list = r.json()['list']  
	count = r.json()['count']
	content_list = []
	for res in res_list:
		item = {}
		# item['user_name'] = res['user']['screen_name']
		item['comment'] = res['text']
		item['comment'] = re.sub("<.*?>||&nbsp;||\`",'', item['comment'])
		# item['comment'] = item['comment'] + csv_delim
		content_list.append(item)
	return content_list, count

# Parameters for random labels
label_cat_cnt_L = 1
label_cat_cnt_R = 16 + 1
label_cnt_L = 1
label_cnt_R = 8 + 1
label_selected_cnt_L = 1
label_selected_cnt_R = 20 + 1
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
	# ??????????????????
	for res in res_list:
		item = {}
		# item['user_name'] = res['user']['screen_name']
		str_parts = []

		# ????????????
		str_parts.append(re.sub("<.*?>||&nbsp;||\`",'', res['text'])) # ????????????????????????????????????????????????

		# ??????????????????????????????
		label_cat_cnt = random_uniform_int(label_cat_cnt_L, label_cat_cnt_R)
		for i in range(label_cat_cnt): # ???????????????????????????
			str_parts.append(csv_delim)
			# ???????????????????????????
			label_cat_len = random_uniform_int(label_cat_len_without_suffix_L, label_cat_len_without_suffix_R)
			for j in range(label_cat_len): # ?????????????????????????????????????????????
				str_parts.append(random_char(random_char_L, random_char_R))
			str_parts.append(str(random_uniform_int(name_dsuffix_L, name_dsuffix_R))) # ????????????????????????????????????????????????

			# ?????????????????????????????????????????????
			label_cnt = random_uniform_int(label_cnt_L, label_cnt_R)
			for j in range(label_cnt):
				# ????????????
				str_parts.append(' ')
				label_len_without_suffix = random_uniform_int(label_len_without_suffix_L, label_len_without_suffix_R)
				for k in range(label_len_without_suffix): # ????????????????????????????????????
					str_parts.append(random_char(random_char_L, random_char_R))
				str_parts.append(str(random_uniform_int(name_dsuffix_L, name_dsuffix_R))) # ?????????????????????????????????????????????
				# ???????????????????????????
				str_parts.append(' ')
				str_parts.append(str(random_uniform_int(label_selected_cnt_L, label_selected_cnt_R)))

		# ??????????????????????????????????????????????????????
		item['comment'] = ''.join(str_parts)
		content_list.append(item)
	return content_list, count

params = {}

def deserialize_param():
	for i in range(1, len(sys.argv)):
		pos = sys.argv[i].find('=')
		key = sys.argv[i][2: pos]
		if pos != -1:
			key = sys.argv[i][2: pos]
			value = sys.argv[i][pos + 1: len(sys.argv[i])]
			if value[0] == '"' and value[-1] == '"': # ??????????????????????????????
				value = value[1: len(value)-1]
			params[key] = value
		else:
			key = sys.argv[i][2: ]
			params[key] = ""


# Parameters for crawling
deserialize_param()
print(params)

access_delay = 3 if "access-delay" not in params else int(params["access-delay"])		# ????????????
retry_delay = 5 if "retry-delay" not in params else int(params["retry-delay"])			# ????????????
page_count = 100 if "pages" not in params else int(params["pages"])						# ????????????

file_name = params["file"]																# ??????????????????
ticker_symbol = params["stock"]															# ?????????????????????
encoding = "gbk" if "encoding" not in params else params["encoding"]					# ?????????????????????
headers["Cookie"] = params["cookie"]													# Cookie

for i in range(page_count):
	detail_url = "https://xueqiu.com/statuses/search.json?count=10&comment=0&symbol={}&hl=0&source=all&sort=&page={}&q=&type=11".format(ticker_symbol, i + 1)
	try:
		if ("random" in params):
			print("????????????" + detail_url + " ????????????????????????????????????")
			comment_data, count = parse_comment_url_with_random_test_labels(detail_url)
		else:
			print("????????????" + detail_url)
			comment_data, count = parse_comment_url(detail_url)
		time.sleep(access_delay)
	except Exception as e: 
		print("Error: ", e)
		time.sleep(retry_delay)
		if ("random" in params):
			print("????????????" + detail_url + " ????????????????????????????????????")
			comment_data, count = parse_comment_url_with_random_test_labels(detail_url)
		else:
			print("????????????" + detail_url)
			comment_data, count = parse_comment_url(detail_url)
		time.sleep(retry_delay)
	save_file(comment_data, encoding)