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
            # "Cookie": "device_id=1bc3f7e62a55294135b256919bad7df4; s=c814xxmy21; bid=55919114a627b9cdf7c2cf6d3b021c7e_km95yxdt; acw_tc=2760820616190675986288295e4abf1a86f8125c58b7e47386a040d5cdea12; Hm_lvt_1db88642e346389874251b5a1eded6e3=1619067601; xq_a_token=4296d3528edb68f8e29363040c82486510a38e74; xqat=4296d3528edb68f8e29363040c82486510a38e74; xq_r_token=bdb802dc27dfb8a683ced0c98ce059de3af61b85; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjY4NzIyNzEzNzcsImlzcyI6InVjIiwiZXhwIjoxNjIxNjU5NjIyLCJjdG0iOjE2MTkwNjc2MjIxNTksImNpZCI6ImQ5ZDBuNEFadXAifQ.mfm1pkInkb6__-yifL7xWMOdV8Sl5PZawnsKnRv1kTVy66FUQ8S2CTbLVN2MacjulkY6hSU_SBVeQhNvrj832TvU9pDPG0xoH4pWPlWCB3MgafEo2fPVrN_lzlwcBlFjLfJKnHfZWCyRnTrqXCreO7iEgMbL3CLj10tNSc3CwfowOW-xzM0vNBoU18YzAV2cnYBEPE9kEAJZPB9NT5cpLqgiyo2LxLPUAgr5_5oZQiGpbA5MN7sxKKuDsbJxRolfIH66QIMHdXlZnTnool1YYoyV6C3juzSp5jWjDMJ0TynIrbxTJHuSzbk4CUZcs2-MaXC6dClOiBO8A2_7s37toQ; xq_is_login=1; u=6872271377; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1619067675"
            "Cookie": "device_id=24700f9f1986800ab4fcc880530dd0ed; s=c511yn1n8b; acw_tc=2760820816228937868421541e3f5749ce5aec8979da60b3c8719be2aba40c; is_overseas=0; Hm_lvt_1db88642e346389874251b5a1eded6e3=1621666964,1621826340,1622893795; xq_a_token=c68b6ee9af2393c44a5d9745eb2857bb6943cef6; xqat=c68b6ee9af2393c44a5d9745eb2857bb6943cef6; xq_r_token=cbfa462352b70532e0a62c9cf3d211cb6569f348; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjEzNjc0MTYyNDEsImlzcyI6InVjIiwiZXhwIjoxNjI1NDg1ODAyLCJjdG0iOjE2MjI4OTM4MDIzNjksImNpZCI6ImQ5ZDBuNEFadXAifQ.jJorr34P0Qm0FArk9vBVvr0u6Xj7hTQdXzlc_ZynM4tlipCS1qCtVwWlEEr13DJ-_3sLgOirBsf9qbCZDj8NggHbSjp8xhtpXOOZbe8Vx1j0bX2XZS_6DjmUEHJ_KWQYoKoPOGRptph0hgQowUVMKlzbnQinJBZmklUkXANXR3pxzHNRolcCYKCx7LHVVEWnpP-HyDxhwTMxbtdmkPwY3ivffufc04UnZ6FgkfV4vhw2PkdIgrvYOzMUWHn21kmosSaVY4L1vIiau8t8l3bJtynMpHaiW-YJY175nHpCt_VQ8EM3g28uZFI8gH81OOrHnv_4yz22XunLDX6bV5jKtA; xq_is_login=1; u=1367416241; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1622893810; snbim_minify=true"
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

stock_num = 'SH000688'  # 输入股票代码
for i in range(100):
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