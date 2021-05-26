import requests
import time
import re
import urllib3
import sys

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
proxies = {
}
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
        with open('xueqiu.csv', 'a')as f:
            f.write(str(content['comment']).encode("gbk", 'ignore').decode("gbk", "ignore"))
            f.write("\n")
            
def parse_comment_url( url):
    r = requests.get(url, headers=headers, proxies=proxies, verify=False)  
    res_list = r.json()['list']  
    count = r.json()['count']
    content_list = []
    for res in res_list:
        item = {}
        #item['user_name'] = res['user']['screen_name']
        item['comment'] = res['text']
        item['comment'] = re.sub("<.*?>||&nbsp;||\`",'',item['comment'])
        item['comment'] = item['comment']+'`'
        content_list.append(item)
    return content_list, count

stock_num = sys.argv[1]  # 输入股票代码
for i in range(5):
    detail_url = "https://xueqiu.com/statuses/search.json?count=10&comment=0&symbol={}&hl=0&source=all&sort=&page={}&q=&type=11".format(stock_num, i + 1)
    print("正爬取"+detail_url)
    try:
        comment_data, count = parse_comment_url(detail_url)
        time.sleep(3)  
    except Exception as e: 
        print("Error:", e)
        time.sleep(5)
        comment_data = parse_comment_url(detail_url)  
        time.sleep(5)
    save_file(comment_data)