import { fetchDictMap } from '@/api/system/dict'
import { defineStore } from 'pinia'
import { isNull } from '@/utils'

export const dictStore = defineStore('dict', () => {
  let dicts = {}
  const defaultPageSizes = 10
  let pageArray = []

  function initDict () {
    return new Promise((resolve, reject) => {
      fetchDictMap().then(response => {
        const data = response.data.data
        if (isNull(data)) {
          resolve(response)
          return
        }
        const value = []
        Object.keys(data).forEach(key => {
          const dictList = data[key]
          dictList.forEach(item => {
            const dict = item
            dict.text = dict.label
            if (key === 'page') {
              value.push(Number.parseInt(dict.value))
            }
          })
        })
        pageArray = value
        dicts = data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }

  return { dicts, defaultPageSizes, pageArray, initDict }
})
