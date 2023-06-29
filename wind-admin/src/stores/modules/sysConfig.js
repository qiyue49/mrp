import { defineStore } from 'pinia'
import { getUserConfigList } from '@/api/sys/config'
import { reactive, ref } from 'vue'

export const sysConfigStore = defineStore('sysConfig', () => {
  const configList = ref([])
  const config = reactive({})

  function getConfig() {
    return new Promise((resolve, reject) => {
      getUserConfigList().then(res => {
        if (res.data.code === 0) {
          configList.value = res.data.data
          configList.value.forEach(item => {
            config[item.code] = item.value
          })
          localStorage.setItem('loginPage', config.loginPage)
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
  return { configList, config, getConfig }
})
