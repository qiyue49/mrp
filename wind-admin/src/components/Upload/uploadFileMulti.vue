<template>
  <el-upload
    ref="upload"
    v-model:file-list="fileList"
    class="upload-demo"
    :data="uploadData"
    :headers="myHeaders"
    :action="uploadImageUrl"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :on-success="handleSuccess"
    :on-error="handleError"
    :before-upload="beforeUpload"
    :auto-upload="false">
    <template #trigger>
      <el-button size="small" type="primary">选取文件</el-button>
    </template>
    <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
    <template #tip>
      <div class="el-upload__tip">{{ waringLabel }}</div>
    </template>
  </el-upload>
</template>

<script>
import { getToken } from '@/utils/auth'
export default {
  name: 'UploadFileMulti',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    dir: {
      type: String,
      default: ''
    },
    extension: {
      type: String,
      default: ''
    },
    uploadLabel: {
      type: String,
      default: '上传文件'
    },
    limit: {
      type: Number,
      required: false,
      default: 10
    },
    basePath: {
      type: String,
      default: ''
    },
    showWaringLabel: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      uploadImageUrl: import.meta.env.VITE_APP_BASE_API + '/oss/attachment/upload',
      uploadData: { base_path: this.basePath },
      myHeaders: { access_token: getToken() },
      imageExtension: ['bmp', 'jpg', 'jpeg', 'png', 'gif'],
      extensions: [],
      fileList: [],
      flag: false,
      uploadLoading: false,
      resultUrl: undefined,
      waringLabel: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        if (this.isNull(val)) {
          this.fileList = []
          return
        }
        if (this.flag) {
          return
        }
        try {
          const array = JSON.parse(val)
          array.forEach(item => {
            const file = {}
            file.name = item.name
            file.status = 'success'
            file.response = { data: item.url }
            this.fileList.push(file)
          })
        } catch (e) {
          const array = val.split(',')
          array.forEach(item => {
            const file = {}
            file.name = item
            file.status = 'success'
            file.response = { data: item }
            this.fileList.push(file)
          })
        }
      }
    }
  },
  created() {
    if (!this.isNull(this.extension)) {
      let extension = this.extension.replace(/(^\s+)|(\s+$)/g, '')
      extension = extension.replace(/\s/g, '')
      this.extensions = extension.split(',')
    }
    if (this.dir !== '') {
      this.uploadImageUrl += '?dir=' + this.dir
    }
    if (this.extensions.length === 0) {
      this.waringLabel = '只能上传不超过' + this.limit + 'M的文件'
    } else {
      this.waringLabel = '只能上传' + this.extension + '文件，且不超过' + this.limit + 'M'
    }
  },
  beforeMount() {
    this.fileList = []
  },
  methods: {
    submitUpload() {
      this.$refs.upload.submit()
    },
    handleRemove(file, fileList) {
      this.emitList(fileList)
    },
    handlePreview(file) {
      if (file.status !== 'success') {
        this.$message.warning('该文件还未成功上传')
        return
      }
      window.open(file.response.data)
    },
    emitInput(val) {
      this.flag = true
      this.$emit('update:modelValue', val)
      this.$nextTick(() => {
        this.flag = false
      })
    },
    emitList(fileList) {
      const urlArray = []
      fileList.forEach(item => {
        if (item.status === 'success') {
          urlArray.push({ name: item.name, url: item.response.data })
        }
      })
      const str = JSON.stringify(urlArray)
      this.emitInput(str)
    },
    handleSuccess(response, file, fileList) {
      if (response.code === 0) {
        this.uploadLoading = false
        this.emitList(fileList)
      } else {
        this.$message.error(response.msg)
      }
    },
    handleError(response, file, fileList) {
      this.uploadLoading = false
      if (response.msg) {
        this.$message.error(response.msg)
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeUpload(file) {
      // 扩展名判断
      const fileExtension = this.getFileExtension(file.name)
      if (fileExtension === '') {
        this.$message.error('文件必须有扩展名')
        return false
      }

      if (this.extension !== '') {
        const isFileExtension = this.extensions.includes(fileExtension)
        if (!isFileExtension) {
          this.$message.error('上传文件只能是' + this.extension + '格式!')
          return false
        }
      }
      // 判断文件大小
      const isLimitSize = file.size / 1024 / 1024 < this.limit
      if (!isLimitSize) {
        this.$message.error('上传头像图片大小不能超过 ' + this.limit + 'MB!')
      }
      if (isLimitSize) {
        this.uploadLoading = true
      }
      return isLimitSize
    },
    getFileExtension(fileName) {
      if (fileName.lastIndexOf('.') > 0) {
        return fileName.substring(fileName.lastIndexOf('.') + 1)
      }
      return ''
    }
  }
}
</script>

<style>
.uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.uploader .el-upload:hover {
  border-color: #409EFF;
}
.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.waring-label {
  font-size: 14px;
  color: #9b9d07;
}
</style>
