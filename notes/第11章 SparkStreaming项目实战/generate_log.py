#!/usr/bin/python
# coding=UTF-8
import random
import time

url_path = [
    "class/112.html",
    "class/128.html",
    "class/145.html",
    "class/146.html",
    "class/131.html",
    "class/130.html",
    "learn/821",
    "course/list"
]

ip_slices = [132, 156, 124, 10, 29, 167, 143, 187, 30, 46, 55, 63, 72, 87, 98, 168]


http_refers = [
    "http://www.baidu.com/s?wd={query}",
    "http://www.sogou.com/web?query={query}",
    "http://cn.bing.com/search?q={query}",
    "http://search.yahoo.com/search?p={query}"
]


search_keyword = [
    "Spark SQL实战",
    "Hadoop基础",
    "Storm实战",
    "Spark Streaming实战",
    "大数据面试"
]


status_codes = ["200", "404", "500"]


def sample_url():
    return random.sample(url_path, 1)[0]
    pass


def sample_ip():
    slices = random.sample(ip_slices, 4)
    return ".".join([str(item) for item in slices])
    pass


def sample_refer():
    if random.uniform(0, 1) > 0.2:
        return "-"
    refer_str = random.sample(http_refers, 1)
    query_str = random.sample(search_keyword, 1)
    return refer_str[0].format(query=query_str[0])
    pass


def sample_code():
    return random.sample(status_codes, 1)[0]
    pass


def generate_log(count=10):
    time_str = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    while count >= 1:
        query_log = "{local_time}\t{url}\t{ip}\t{refer}\t{code}"\
            .format(local_time=time_str, url=sample_url(), ip=sample_ip(), refer=sample_refer(), code=sample_code())
        print query_log
        count = count - 1
    pass


if __name__ == '__main__':
    generate_log()
    pass
