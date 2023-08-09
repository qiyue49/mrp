<template>
  <div ref="qrCode"></div>
</template>
<script>
import QRCode from 'qrcodejs2-fix'
import { objectMerge } from '@/utils'
export default {
  name: 'QrCode',
  props: {
    text: {
      type: String,
      required: true
    },
    options: {
      type: Object,
      default: undefined
    }
  },
  data() {
    return {
      qrCode: undefined
    }
  },
  watch: {
    text: {
      immediate: true,
      handler(val) {
        if (!this.qrCode) {
          return
        }
        if (this.isNull(val)) {
          return
        }
        this.qrCode.clear()
        this.qrCode.makeCode(val)
      }
    }
  },
  mounted() {
    this.createQrcode()
  },
  methods: {
    createQrcode() {
      let option = {
        text: this.text,
        width: 200,
        height: 200
      }
      option = objectMerge(option, this.options)
      console.log('option', option, this.options)
      this.qrCode = new QRCode(this.$refs.qrCode, option)
    }
  }
}
</script>
