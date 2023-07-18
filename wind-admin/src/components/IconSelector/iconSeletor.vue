<template>
  <div style="width: 100%">
    <el-input v-model="name" readonly>
      <template #prefix>
        <el-icon v-if="name" class="icon"><component :is="name"/></el-icon>
      </template>
      <template #append>
        <el-button icon="Search" @click="show" />
      </template>
    </el-input>
    <el-dialog v-model="iconFormVisible" draggable class="dialog-title" title="选择图标" :close-on-click-modal="false" append-to-body>
      <el-form>
        <el-row :gutter="20">
          <el-input v-model="name" suffix-icon="Search" placeholder="请输入图标名称" clearable @input="filterIcons"/>
          <el-scrollbar height="400px" style="margin-top:20px; width: 100%">
            <el-row>
              <el-col
                v-for="item in iconList" :key="item" :span="6" :class="{'active': item === isActive}"
                @click="activeFun(item)">
                <component :is="item" class="icon"/>
                <span class="svg-name">{{ item }}</span>
              </el-col>
            </el-row>
          </el-scrollbar>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="iconFormVisible = false">取消</el-button>
        <el-button type="primary" @click="addCreateData()">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: 'IconSelector',
  props: {
    modelValue: undefined
  },
  emits: ['update:modelValue'],
  data() {
    return {
      isActive: undefined,
      name: undefined,
      iconList: this.$icons,
      iconFormVisible: false
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
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
        this.iconList = this.$icons.filter(item => item.toLowerCase().includes(this.name.toLowerCase()))
      } else {
        this.iconList = this.$icons
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
      this.$emit('update:modelValue', this.name)
      this.iconFormVisible = false
    },
    clear() {
      this.$emit('update:modelValue', undefined)
      this.iconFormVisible = false
    }
  }
}
</script>
<style scoped>
.svg-name{
  vertical-align: middle;
  white-space: nowrap; /*文本不自动换行*/
  overflow: hidden; /*隐藏超出容器的文本*/
  text-overflow: ellipsis; /*省略显示*/
}
</style>

