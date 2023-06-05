import { fetchDictMap } from '@/api/sys/dict'
import { defineStore } from 'pinia'
import { isNull } from '@/utils'
import { ref } from 'vue'

export const dictStore = defineStore('dict', () => {
  const dicts = ref({})
  const defaultPageSize = ref(10)
  const pageArray = ref([])

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
        pageArray.value = value
        defaultPageSize.value = pageArray.value[0]
        dicts.value = data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }

  return { dicts, defaultPageSize, pageArray, initDict }
})
