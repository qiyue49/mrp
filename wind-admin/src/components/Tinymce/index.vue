<template>
  <!-- 富文本 -->
  <div class="tinymce-container">
    <editor :id="tinymceId" v-model="content" class="tinymce-textarea" :init="init" :disabled="disabled"/>
  </div>
</template>

<script>
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/models/dom' // 特别注意 tinymce 6.0.0 版本之后必须引入，否则不显示
import 'tinymce/icons/default/icons'
import 'tinymce/themes/silver'
import 'tinymce/plugins/accordion'
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/anchor'
import 'tinymce/plugins/autolink'
import 'tinymce/plugins/autoresize'
import 'tinymce/plugins/autosave'
import 'tinymce/plugins/charmap'
import 'tinymce/plugins/code'
import 'tinymce/plugins/codesample'
import 'tinymce/plugins/directionality'
import 'tinymce/plugins/emoticons'
import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/help'
import 'tinymce/plugins/image'
import 'tinymce/plugins/importcss'
import 'tinymce/plugins/insertdatetime'
import 'tinymce/plugins/link'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/media'
import 'tinymce/plugins/nonbreaking'
import 'tinymce/plugins/pagebreak'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/quickbars'
import 'tinymce/plugins/save'
import 'tinymce/plugins/searchreplace'
import 'tinymce/plugins/table'
import 'tinymce/plugins/visualblocks'
import 'tinymce/plugins/visualchars'
import 'tinymce/plugins/wordcount'
// 扩展插件
// import '../assets/tinymce/plugins/lineheight/plugin'
// import editorImage from './components/editorImage'

export default {
  name: 'Tinymce',
  components: {
    Editor
  },
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    height: {
      type: Number,
      default: 600
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default:
          'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media code codesample table charmap nonbreaking insertdatetime advlist lists wordcount autosave autoresize'
    },
    toolbar: {
      type: [String, Array],
      default: 'styleselect formatselect fontselect fontsizeselect |\ undo redo restoredraft cut copy paste pastetext forecolor backcolor bold italic underline strikethrough link codesample  fullscreen preview code| alignleft aligncenter alignright alignjustify outdent indent formatpainter bullist numlist blockquote subscript superscript removeformat table image media charmap pagebreak insertdatetime'
    }
  },
  emits: ['update:modelValue'],
  data() {
    const dir = import.meta.env.MODE === 'tomcat' ? '/admin' : ''
    return {
      tinymceId: this.id || 'vue-tinymce' + Date.parse(new Date()),
      // 初始化配置
      init: {
        language_url: dir + '/static/tinymce/langs/zh-Hans.js',
        language: 'zh-Hans',
        skin_url: dir + '/static/tinymce/skins/ui/oxide',
        content_css: dir + '/static/tinymce/skins/content/default/content.css',
        height: this.height,
        image_dimensions: false, // 去除宽高属性
        min_height: 600,
        max_height: 2000,
        removed_menuitems: 'template', // 去除模板
        toolbar_mode: 'wrap',
        plugins: this.plugins,
        toolbar: this.toolbar,
        content_style: 'p {margin: 5px 0;}',
        fontsize_formats: '12px 14px 16px 18px 24px 36px 48px 56px 72px',
        font_formats:
            '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
        branding: false,
        promotion: false,
        tinymceId: this.tinymceId,
        // 此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        // 如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        images_upload_handler: (blobInfo, progress) => {
          return new Promise((resolve, reject) => {
            resolve('data:' + blobInfo.blob().type + ';base64,' + blobInfo.base64())
          })
        }
      },
      content: this.modelValue
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.content = val
      }
    },
    content: {
      immediate: true,
      handler(val) {
        this.$emit('update:modelValue', val)
      }
    }
  },
  mounted() {
    tinymce.init(this.init)
  }
}
</script>
