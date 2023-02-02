<template>
  <div>
    <el-input v-model="name" readonly >
      <template #append>
        <el-button :icon="Search" @click="show" />
      </template>
    </el-input>
    <el-dialog v-model="iconFormVisible" title="选择图标" :close-on-click-modal="false" append-to-body>
      <el-form :model="temp" style="width: 90%; margin-left:9%;">
        <el-input v-model="name" suffix-icon="Search" placeholder="请输入图标名称" @input="filterIcons"/>
        <el-scrollbar height="400px">
          <el-row>
            <el-col
              v-for="item in iconList" :key="item" :span="8" class="colBox" :class="{'active': item === isActive}"
              @click="activeFun(item)">
              <svg-icon :icon-class="item"/>
              <span class="col-span">{{ item }}</span>
            </el-col>
          </el-row>
        </el-scrollbar>
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
  emits: ['iconSelect'],
  data() {
    return {
      isActive: undefined,
      name: undefined,
      iconList: svgIcons,
      iconFormVisible: false
    }
  },
  methods: {
    show() {
      this.iconFormVisible = true
    },
    filterIcons() {
      if (this.name) {
        this.iconList = this.svgIcons.filter(item => item.includes(this.name))
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

<style scoped>

</style>
