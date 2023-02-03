<template>
  <div>
    <el-input v-model="name" readonly style="width: 100%">
      <template #prepend>
        <svg-icon :icon-class="name" />
      </template>
      <template #append>
        <el-button icon="Search" @click="show" />
      </template>
    </el-input>
    <el-dialog v-model="iconFormVisible" title="选择图标" :close-on-click-modal="false" append-to-body>
      <el-form style="width: 90%; margin-left:9%;">
        <el-row :gutter="20">
          <el-input v-model="name" suffix-icon="Search" placeholder="请输入图标名称" @input="filterIcons"/>
          <el-scrollbar height="400px" style="margin-top:20px">
            <el-row>
              <el-col
                v-for="item in iconList" :key="item" :span="6" :class="{'active': item === isActive}"
                @click="activeFun(item)">
                <svg-icon :icon-class="item"/>
                <span class="col-span">{{ item }}</span>
              </el-col>
            </el-row>
          </el-scrollbar>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="iconFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addCreateData($event)">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import svgIcons from '@/components/IconSelector/svg-icons'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'IconSelector',
  components: { SvgIcon },
  props: {
    value: {
      type: String,
      required: true
    }
  },
  emits: ['input'],
  data() {
    return {
      isActive: undefined,
      name: undefined,
      iconList: svgIcons,
      iconFormVisible: false
    }
  },
  watch: {
    value: {
      immediate: true,
      handle(val) {
        this.name = val
      }
    }
  },
  methods: {
    show() {
      this.iconFormVisible = true
    },
    filterIcons() {
      if (this.name) {
        this.iconList = svgIcons.filter(item => item.includes(this.name))
      } else {
        this.iconList = svgIcons
      }
    },
    activeFun: function(item) {
      this.isActive = item
      this.name = item
    },
    addIcon() {
      this.iconFormVisible = true
    },
    addCreateData() {
      this.$emit('input', this.name)
      this.iconFormVisible = false
    }
  }
}
</script>

