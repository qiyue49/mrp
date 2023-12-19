<template>
  <div class="upload-container">
    <el-upload
      v-model:file-list="imageList"
      :data="uploadData"
      :action="uploadImageUrl"
      accept="image/*"
      list-type="picture-card"
      :limit="maxCount"
      :headers="myHeaders"
      :on-success="handleUploadSuccess"
      :before-upload="beforeUpload"
      :on-preview="handlePictureCardPreview"
      :on-remove="remove"
      :class="{hideImg:noneBtnImg}"
    >
      <el-icon>
        <Plus/>
      </el-icon>
    </el-upload>
    <el-dialog v-model="dialogVisible" draggable>
      <img :src="dialogImageUrl" alt="Preview Image" style="width:100%;object-fit: cover"/>
    </el-dialog>
  </div>
</template>

<script>
import { getRefreshToken, getToken } from '@/utils/auth'
import { refreshToken } from '@/api/sys/oauth2'

export default {
  name: 'UploadImage',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    dir: {
      type: String,
      default: ''
    },
    basePath: {
      type: String,
      default: ''
    },
    maxCount: {
      type: Number,
      default: 4
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      imageList: [],
      noneBtnImg: false,
      resultUrl: undefined,
      dialogImageUrl: undefined,
      dialogVisible: false,
      myHeaders: { access_token: getToken() },
      uploadImageUrl: import.meta.env.VITE_APP_BASE_API + '/oss/attachment/upload',
      uploadData: { base_path: this.basePath }
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.imageList = []
        this.noneBtnImg = false
        if (this.isNull(val)) {
          return
        }
        try {
          this.imageList = JSON.parse(val)
        } catch {
          val.split(',').forEach(item => {
            this.imageList.push({ name: item, url: item })
          })
        }
        this.noneBtnImg = this.imageList.length >= this.maxCount
      }
    }
  },
  created() {
    if (this.dir !== '') {
      this.uploadImageUrl += '?dir=' + this.dir
    }
  },
  methods: {
    handlePictureCardPreview(uploadFile) {
      this.dialogImageUrl = uploadFile.url
      this.dialogVisible = true
    },
    remove() {
      this.emitInput(this.imageList)
    },
    emitInput(val) {
      if (this.isNull(val)) {
        this.$emit('update:modelValue', undefined)
      } else {
        this.$emit('update:modelValue', JSON.stringify(val))
      }
      this.noneBtnImg = this.imageList.length >= this.maxCount
    },
    handleUploadSuccess(response, file) {
      this.imageList = this.imageList.map(item => {
        return { name: item.name, url: response.data }
      })
      if (response.code === 0) {
        // this.imageList.push(response.data)
        this.emitInput(JSON.parse(JSON.stringify(this.imageList)))
      } else if (response.code === 200004) {
        const refreshTokenData = getRefreshToken()
        refreshToken(refreshTokenData).then(res => {
          if (res.data.code === 0) {
            this.$store.userStore.setToken(res.data.data.accessToken)
            this.myHeaders = { access_token: res.data.data.accessToken }
            file.status = 'ready'
            // this.$refs.upload.$forceUpdate()
            this.$refs.upload.submit()
          } else {
            this.$message.error('Token失效，请重新登录')
          }
        })
      } else {
        this.$message.error(response.msg)
      }
    },
    handleError(response) {
      if (response.msg) {
        this.$message.error(response.msg)
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeUpload(file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style lang="scss" scoped>
.hideImg :deep(.el-upload--picture-card) {
  display: none;
}
</style>
