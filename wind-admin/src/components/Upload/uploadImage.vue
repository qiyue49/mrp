<template>
  <div
    class="upload-container">
    <el-upload
      v-model:file-list="imageList"
      :data="uploadData"
      :action="uploadImageUrl"
      list-type="picture-card"
      :limit="maxCount"
      :headers="myHeaders"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :on-preview="handlePictureCardPreview"
      :on-remove="remove"
      :on-change="dealImgChange"
      class="avatar-uploader"
      :class="{hideImg:noneBtnImg}"
    >
      <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload>
    <el-dialog v-model="dialogVisible" draggable class="dialog-title">
      <img :src="dialogImageUrl" alt="Preview Image" style="width:100%;object-fit: cover" />
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

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
    span: {
      type: Number,
      default: 6
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
  computed: {
    imageUrl() {
      return this.resultUrl
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        if (this.isNull(val)) {
          this.imageList = []
          return
        }
        this.imageList = JSON.parse(val)
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
      // this.imageList.splice(this.imageList.indexOf(uploadFile), 1)
      this.emitInput(this.imageList)
      this.noneBtnImg = this.imageList.length >= this.maxCount
    },
    dealImgChange() {
      this.noneBtnImg = this.imageList.length >= this.maxCount
    },
    emitInput(val) {
      this.$emit('update:modelValue', JSON.stringify(val))
    },
    handleAvatarSuccess(response, file) {
      this.imageList = this.imageList.map(item => {
        return { name: item.name, url: response.data }
      })
      if (response.code === 0) {
        // this.imageList.push(response.data)
        this.emitInput(JSON.parse(JSON.stringify(this.imageList)))
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
    beforeAvatarUpload(file) {
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
  $value: --el-upload-list-picture-card-size;
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    width: $value;
    height: $value;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  :deep(.el-upload):hover {
    border-color: var(--el-color-primary);
  }

  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: $value;
    height: $value;
    text-align: center;
  }

  .avatar-uploader {
    width: $value;
    height: $value;
    margin-top: 10px;
    display: block;
  }

  .hideImg :deep(.el-upload--picture-card){
    display: none;
  }

  .upload-container{
    :deep(.el-overlay){
      z-index: 201000000 !important;
    }
  }

  .delete {
    position: absolute;
    margin-left: -30px;
    margin-top: 3px

  }
</style>
