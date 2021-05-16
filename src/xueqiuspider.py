import requests
import time
import re
import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
proxies = {
}
start_url = 'https://xueqiu.com/service/v5/stock/screener/quote/list?page={}&size=30&order=desc&order_by=percent&exchange=CN&market=CN&type=sha&'
headers = {
            "Host": "xueqiu.com",
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36",
            "Referer": "https://xueqiu.com/hq",
            # "Cookie": "device_id=bd43492b7a85e5d94101bda778256023; acw_tc=2760820116154239892348120e59513794f6b3b1c60e440e76334fdd0f9bac; Hm_lvt_1db88642e346389874251b5a1eded6e3=1614869350,1615026899,1615026916,1615423990; s=ea12gxzx1g; xq_a_token=1f2f0960b6b7cfbc2610260a7e6874a67c334e88; xqat=1f2f0960b6b7cfbc2610260a7e6874a67c334e88; xq_r_token=4b3f2b3595277469bbb479ffc44c13a556b82c1a; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjU3OTE0MjkxNTIsImlzcyI6InVjIiwiZXhwIjoxNjE4MDE2MDYzLCJjdG0iOjE2MTU0MjQwNjM3NDUsImNpZCI6ImQ5ZDBuNEFadXAifQ.EUtf3vCgr_hP-XVpLIZOWabgztEiiHK3TE_u3oJAkm4r1Nusa51hQP2K6vQES5RUJNHy_bZrsagg1inn1EcLrU-2V5ke388HpsDiJnv_wB_9wx74wWMXhveuOMuC8Hv6NBAdNfrfEP4AxA5RnRqJEzbmq9S4AzzsJpBi3_UugtCsZpGMTC6r7628hkgff1rtQZ4OBG2aQH2kEivkskGBz0crcrnYEsiSVzwun6v6Ltit9qY81UtYRJBNsAZEvh1-993x8i06sYDfseQNcH56rLEpnazIK0Gg0vgqmeBBLrSZZHbdD1-5nJuBOgdfhgIwtnX8BceQX3Kl5Qq8ZOhYUA; xq_is_login=1; u=5791429152; snbim_minify=true; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1615424131"
            "Cookie": "device_id=1bc3f7e62a55294135b256919bad7df4; s=c814xxmy21; bid=55919114a627b9cdf7c2cf6d3b021c7e_km95yxdt; acw_tc=2760820616190675986288295e4abf1a86f8125c58b7e47386a040d5cdea12; Hm_lvt_1db88642e346389874251b5a1eded6e3=1619067601; xq_a_token=4296d3528edb68f8e29363040c82486510a38e74; xqat=4296d3528edb68f8e29363040c82486510a38e74; xq_r_token=bdb802dc27dfb8a683ced0c98ce059de3af61b85; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjY4NzIyNzEzNzcsImlzcyI6InVjIiwiZXhwIjoxNjIxNjU5NjIyLCJjdG0iOjE2MTkwNjc2MjIxNTksImNpZCI6ImQ5ZDBuNEFadXAifQ.mfm1pkInkb6__-yifL7xWMOdV8Sl5PZawnsKnRv1kTVy66FUQ8S2CTbLVN2MacjulkY6hSU_SBVeQhNvrj832TvU9pDPG0xoH4pWPlWCB3MgafEo2fPVrN_lzlwcBlFjLfJKnHfZWCyRnTrqXCreO7iEgMbL3CLj10tNSc3CwfowOW-xzM0vNBoU18YzAV2cnYBEPE9kEAJZPB9NT5cpLqgiyo2LxLPUAgr5_5oZQiGpbA5MN7sxKKuDsbJxRolfIH66QIMHdXlZnTnool1YYoyV6C3juzSp5jWjDMJ0TynIrbxTJHuSzbk4CUZcs2-MaXC6dClOiBO8A2_7s37toQ; xq_is_login=1; u=6872271377; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1619067675"
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
        item['comment'] = re.sub("<.*?>",'',item['comment'])
        content_list.append(item)
    return content_list, count

# stock_num = 'SH601318'  # 输入股票代码
stock_num = 'NVDA'
# for i in range(2):
for i in range(100):
    detail_url = "https://xueqiu.com/statuses/search.json?count=10&comment=0&symbol={}&hl=0&source=all&sort=&page={}&q=&type=11".format(stock_num, i + 1)
    print("正爬取"+detail_url)
    try:
        comment_data, count = parse_comment_url(detail_url)
        # time.sleep(3)
        time.sleep(1)
    except Exception as e: 
        print("Error:", e)
        # time.sleep(5)
        time.sleep(1)
        comment_data = parse_comment_url(detail_url)  
        # time.sleep(5)
        time.sleep(1)
save_file(comment_data)