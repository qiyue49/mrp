import { defineStore } from 'pinia'
import { getUserConfigList } from '@/api/sys/config'

export const sysConfigStore = defineStore('sysConfig', () => {
  let configList = []
  const config = {}

  function getConfig() {
    return new Promise((resolve, reject) => {
      getUserConfigList().then(res => {
        if (res.data.code === 0) {
          configList = res.data.data
          configList.forEach(item => {
            config[item.code] = item.value
          })
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
  return { configList, config, getConfig }
})
