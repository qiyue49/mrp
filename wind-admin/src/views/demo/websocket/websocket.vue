<template>
  <el-card>
    <div class="app-container">
      websocket样例:在输入框中输入任意信息，点击发送，信息的json串会通过服务器回传回来，显示在下方的textarea中<br />
      相关后台代码见com.sunseagear.wind.modules.websocket包下文件夹
      <br />
      <el-input v-model="content" type="textarea" :rows="20" style="width: 60%" class="filter-item" />
      <el-input v-model="temp.message" style="width: 60%;" />
      <el-radio
        v-for="item in dictList('sf')"
        :key="'type' + item.label"
        v-model="temp.type"
        :label="item.value"
      >
        {{ item.label }}
      </el-radio>
      <el-button :loading="loading" type="primary" icon="Download" @click="sendMessage">
        发送
      </el-button>
    </div>
  </el-card>

</template>

<script>
import { webSocketConnect, webSocketSend, webSocketClose } from '@/utils/webSocket'
export default {
  name: 'Websocket',
  data() {
    return {
      message: {},
      loading: false,
      temp: {
        message: undefined,
        type: '1'
      },
      content: ''
    }
  },
  created() {
    webSocketConnect(this.onMessage)
  },
  unmounted() {
    webSocketClose()
  },
  methods: {
    sendMessage() {
      this.message.code = 101001
      this.message.success = true
      this.message.message = ''
      this.message.userId = this.$store.userStore.userInfo.id
      this.message.data = this.temp
      this.loading = true
      webSocketSend(this.message)
    },
    onMessage(event) {
      const message = JSON.parse(event.data)
      this.loading = false
      if (message.success) {
        this.content += '\n' + JSON.stringify(message.data)
      } else {
        this.$message.error({ dangerouslyUseHTMLString: true, message: message.message })
      }
    }
  }
}
</script>

<style scoped>

</style>
