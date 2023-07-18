<template>
  <el-card>
    <el-row :gutter="40">
      <el-col :span="6">
        <div class="title">图片上传：{{ urlImage }}</div>
        <upload-image v-model="urlImage" dir="test"/>
      </el-col>
      <el-col :span="6">
        <div class="title">文件上传：{{ urlFile }}</div>
        <upload-file v-model="urlFile" dir="test"/>
      </el-col>
      <el-col :span="6">
        <div class="title">多文件上传：{{ urlFileMulti }}</div>
        <upload-file-multi v-model="urlFileMulti" dir="test"/>
      </el-col>
      <el-col :span="6">
        <div class="title">图片列表</div>
        <image-gallery v-model="imageFileList"/>
      </el-col>
      <el-col :span="6">
        <div class="title">文件列表</div>
        <file-download-list v-model="imageFileList"/>
      </el-col>
      <el-col :span="6">
        <div class="title">checkBox{{ checkId }}</div>
        <checkbox v-model="checkId" :list="dictList('sf')"/>
        <div class="title">checkBox-字典{{ checkId }}</div>
        <checkbox-dict v-model="checkId" dict-code="sf"/>
      </el-col>
      <el-col :span="6">
        <div class="title">下拉选择{{ selectId }}</div>
        <select-list v-model="selectId" :list="dictList('sf')"/>
        <div class="title">树形选择{{ treeId }}</div>
        <cascader-list v-model="treeId" :list="treeList"/>
        <div class="title">自动完成{{ selectId }}</div>
        <autocomplete-list v-model="selectId" :list="dictList('sf')" :props="{ value: 'label', id: 'value' }"/>
      </el-col>
      <el-col :span="6">
        <div class="title">SVG图标<svg-icon :icon-class="svgIcon" /></div>
        <svg-icon-selector v-model="svgIcon" />
        <div class="title">用户选择{{ userId }}</div>
        <system-user v-model="userId" />
        <div class="title">组织机构选择{{ orgId }}</div>
        <system-organization v-model="orgId" />
      </el-col>
      <el-col :span="8">
        <div>地图打点：{{ point }}</div>
        <baidu-map-point v-model="point" />
      </el-col>
      <el-col :span="8">
        <div>地图划线：{{ line }}</div>
        <baidu-map-line v-model="line" />
      </el-col>
      <el-col :span="8">
        <div>地图画框：{{ path }}</div>
        <baidu-map-rect v-model="path" />
      </el-col>
    </el-row>
  </el-card>

</template>

<script>

import BaiduMapPoint from '@/components/BaiduMap/baiduMapPoint'
import BaiduMapLine from '@/components/BaiduMap/baiduMapLine.vue'
import BaiduMapRect from '@/components/BaiduMap/baiduMapRect.vue'
import UploadImage from '@/components/Upload/uploadImage.vue'
import UploadFile from '@/components/Upload/uploadFile.vue'
import UploadFileMulti from '@/components/Upload/uploadFileMulti.vue'
import ImageGallery from '@/components/ImageGallery/imageGallery.vue'
import FileDownloadList from '@/components/FileList/fileDownloadList.vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
import SvgIconSelector from '@/components/IconSelector/svgIconSeletor.vue'
import SystemUser from '@/components/System/systemUser.vue'
import SystemOrganization from '@/components/System/systemOrganization.vue'
import SelectList from '@/components/Select/selectList.vue'
import CascaderList from '@/components/Select/cascaderList.vue'
import AutocompleteList from '@/components/Select/autocompleteList.vue'
import Checkbox from '@/components/Checkbox/checkbox.vue'
import CheckboxDict from '@/components/Checkbox/checkboxDict.vue'
import { fetchTreeTableList } from '@/api/demo/treeTable/treeTable'

const imageList = [
  { name: '文件1.jepg', url: 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg' },
  { name: '文件2.jepg', url: 'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg' },
  { name: '文件3.jepg', url: 'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg' },
  { name: '文件4.jepg', url: 'https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg' },
  { name: '文件5.jepg', url: 'https://fuss10.elemecdn.com/d/e6/c4d93a3805b3ce3f323f7974e6f78jpeg.jpeg' },
  { name: '文件6.jepg', url: 'https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg' },
  { name: '文件7.jepg', url: 'https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg' }
]
export default {
  name: 'ComponentSample',
  components: {
    CheckboxDict,
    Checkbox,
    AutocompleteList,
    CascaderList,
    SelectList,
    SystemOrganization,
    SystemUser,
    SvgIconSelector,
    SvgIcon,
    FileDownloadList,
    ImageGallery,
    UploadFileMulti,
    UploadFile,
    UploadImage,
    BaiduMapRect,
    BaiduMapLine,
    BaiduMapPoint
  },
  data() {
    return {
      point: undefined,
      line: undefined,
      path: undefined,
      urlImage: undefined,
      urlFile: undefined,
      urlFileMulti: undefined,
      imageFileList: JSON.stringify(imageList),
      svgIcon: '',
      userId: undefined,
      orgId: undefined,
      checkId: undefined,
      selectId: undefined,
      treeId: undefined,
      treeList: []

    }
  },
  created() {
    this.getTree()
  },
  methods: {
    getTree() {
      fetchTreeTableList().then(response => {
        if (response.data.code === 0) {
          this.treeList = response.data.data
        } else {
          this.$message.error(response.data.msg)
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.title{
  font-size: 20px;
}
</style>
