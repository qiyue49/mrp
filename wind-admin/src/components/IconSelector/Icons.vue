<template>
  <div class="ui-fas">
    <el-input v-model="name" suffix-icon="Search" placeholder="请输入图标名称" @input="filterIcons" />
    <el-row>
      <el-col v-for="(item, index) in iconList" :key="index" v-model="temp.addIcon" :span="8" class="colBox" :class="{'active': item == isActive}" @click="activeFun($event)">
        <svg-icon :icon-class="item" />
        <span class="col-span">{{ item }}</span>
      </el-col>
    </el-row>
  </div>
</template>
<script>

import svgIcons from '@/components/IconSelector/svg-icons'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'UiIcons',
  components: { SvgIcon },
  data() {
    return {
      isActive: '',
      name: '',
      iconList: svgIcons,
      temp: {
        addIcon: undefined
      }
    }
  },
  methods: {
    filterIcons() {
      if (this.name) {
        this.iconList = this.svgIcons.filter(item => item.includes(this.name))
      } else {
        this.iconList = svgIcons
      }
    },
    selectedIcon(name) {
      this.$emit('selected', name)
    },
    activeFun: function(event) {
      this.isActive = event.target.textContent
      this.temp.addIcon = event.target.textContent
      this.$emit('listenIcon', event.target.textContent)
    }
  }
}
</script>
<style lang="scss" scoped>
  .colBox {
    display: flex;
    align-items: center;
    .col-span {
      padding-left: 5px
    }
  }
</style>
