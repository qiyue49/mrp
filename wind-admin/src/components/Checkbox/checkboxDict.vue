<template>
  <el-checkbox-group v-model="checkList" @change="change">
    <el-checkbox
      v-for="item in dictList(dictCode)"
      :key="'type' + item.value"
      :label="item.value">
      {{ item.label }}
    </el-checkbox>
  </el-checkbox-group>
</template>

<script>
export default {
  name: 'CheckboxDict',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    dictCode: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      checkList: []
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        // console.log('value', val)
        if (val === undefined || val === '') {
          return
        }
        this.checkList = val.split(',').filter(item => {
          return item !== ''
        })
        // console.log('checkList', this.checkList)
      }
    }
  },
  methods: {
    change(val) {
      // console.log('change', val)
      this.$emit('update:modelValue', val.join(','))
    }

  }

}
</script>

<style scoped>

</style>
