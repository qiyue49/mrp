<template>
  <div>
    <div style="display:flex;align-items:center;width:300px;margin:0 auto;padding-bottom:20px;">
      <slide-verify
        ref="slideblock"
        :accuracy="accuracy"
        :slider-text="text"
        @again="onAgain"
        @fulfilled="onFulfilled"
        @success="onSuccess"
        @fail="onFail"
        @refresh="onRefresh"
      />
    </div>
    <div>{{ msg }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      msg: '',
      text: '向右滑',
      // 精确度小，可允许的误差范围小；为1时，则表示滑块要与凹槽完全重叠，才能验证成功。默认值为5
      accuracy: 1

    }
  },
  created() {
    this.lower()
  },
  methods: {
    // 验证成功，触发该方法
    onSuccess(times) {
      const ms = (times / 1000).toFixed(1)
      this.msg = 'login success, 耗时 ' + ms + 's'
    },
    onFail() {
      console.log('验证不通过')
      this.msg = ''
    },
    onRefresh() {
      console.log('点击了刷新小图标')
      this.msg = ''
    },
    onFulfilled() {
      console.log('刷新成功啦！')
    },
    onAgain() {
      console.log('检测到非人为操作的哦！')
      this.msg = 'try again'
      // 刷新
      this.$refs.slideblock.reset()
    },
    handleClick() {
      // 父组件直接可以调用刷新方法
      this.$refs.slideblock.reset()
    },
    handleClose() {
      // console.log("val", val);
    },
    lower() {
      console.log('我是home文件的方法')
    }
  }
}
</script>
<style lang="scss" scoped>
.aaa {
  color: red;
}
.aaa {
  border: 1px solid blue;
}
</style>