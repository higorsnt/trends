import requests
import json

url = 'http://localhost:9200/trends/'

def delete():
    try:
        requests.delete('http://localhost:9200/_all')
    except:
        print(r.raise_for_status())

def setSettings():
    json_settings = open('settings.json', encoding='utf-8').read()
    settings = json.loads(json_settings)

    try:
        requests.put(url, json=settings)
    except:
        print(r.raise_for_status())

def load():
    json_data = open('data.json', encoding='utf-8').read()
    list_post = json.loads(json_data)

    for i in range(len(list_post)):
        try:
            r = requests.put(f'{url}_doc/{i}', json=list_post[i])
        except:
            print(r.raise_for_status())



delete()
setSettings()
load()
print("Ready...")