<template>
  <el-dialog v-model="iconFormVisible" :title="title" :close-on-click-modal="false" append-to-body>
    <el-form ref="addIconForm" :model="temp" label-position="left" label-width="120px" style="width: 90%; margin-left:9%;">
      <el-scrollbar style="height: 550px">
        <ui-icons @listenIcon="showIcon" />
      </el-scrollbar>
    </el-form>
    <template #footer>
      <el-button @click="iconFormVisible = false">取消</el-button>
      <el-button type="primary" @click="addCreateData($event)">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'IconSelector',
  data() {
    return {
      // isActive: '',
      title: '选择图标',
      temp: {
        addIcon: undefined
      },
      iconFormVisible: false,
      iconStatus: 'create'
    }
  },
  methods: {
    addIcon() {
      this.iconStatus = 'create'
      this.iconFormVisible = true
    },
    showIcon: function(data) {
      this.temp.addIcon = data
    },
    addCreateData(event) {
      // 预处理提交的数据
      this.$emit('iconSelect', this.temp.addIcon)
      this.iconFormVisible = false
    }
  }
}
</script>

<style scoped>

</style>
