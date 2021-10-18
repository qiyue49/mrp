import { configList } from '@/api/sys/config'

const state = {
  configList: [],
  config: {}
}

const mutations = {
  SET_CONFIG_LIST: (state, configList) => {
    state.configList = configList
  },
  SET_CONFIG: (state, configList) => {
    configList.forEach(item => {
      state.config[item.code] = item.value
    })
  }
}

const actions = {
  getConfig({ commit }) {
    return new Promise((resolve, reject) => {
      configList().then(res => {
        if (res.data.code === 0) {
          commit('SET_CONFIG_LIST', res.data.data)
          commit('SET_CONFIG', res.data.data)
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
