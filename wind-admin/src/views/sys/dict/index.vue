<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>字典分组</span>
          </template>
          <div class="item">
            <dict-group @refresh-dicts="onRefreshDicts" />
          </div>
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>[{{ dictGroupName }}]字典值</span>
          </template>
          <div class="item">
            <dict ref="dict" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import dictGroup from './components/dictGroup'
import dict from './components/dict'

export default {
  name: 'SysDictList',
  components: { dictGroup, dict },
  data() {
    return {
      dictGroupName: '未选择'
    }
  },
  methods: {
    onRefreshDicts: function(row) {
      this.dictGroupName = row.name
      this.$refs.dict.refreshGroupDict(row)
    }
  }
}
</script>
