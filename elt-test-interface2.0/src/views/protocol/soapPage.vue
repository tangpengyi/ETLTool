<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/protocol/http' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>Soap协议</el-breadcrumb-item>
    </el-breadcrumb>
    <el-divider></el-divider>

    <el-form ref="reqFrom" :model="reqFrom" label-width="80px">
      <el-form-item label="请求地址:">

        <el-input placeholder="请求地址" v-model="reqFrom.reqUrl" style="width:70%" class="input-with-select">
          <el-select v-model="reqFrom.reqStyle" @change="changeSoapReqStyle()" style="width:90px" slot="prepend">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-input>
        <el-button type="primary" @click="reqTest()">发送</el-button>
      </el-form-item>

      <el-form-item label="定时调用">
        <el-switch v-model="reqFrom.isScheduledTasks"></el-switch>
      </el-form-item>

      <el-form-item label="开始时间" v-if="reqFrom.isScheduledTasks" style="width: 500px">
        <el-col :span="11">
          <el-date-picker type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm" placeholder="选择日期"
                          v-model="reqFrom.startTime" style="width: 100%;"></el-date-picker>
        </el-col>
      </el-form-item>

      <el-form-item label="结束时间" v-if="reqFrom.isScheduledTasks" style="width: 500px">
        <el-col :span="11">
          <el-date-picker type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm" placeholder="选择日期"
                          v-model="reqFrom.endTime" style="width: 100%;"></el-date-picker>
        </el-col>
      </el-form-item>

    </el-form>

    <el-divider></el-divider>
    <span>请求参数：</span>

    <el-tabs v-model="reqFrom.activeName" @tab-click="">
      <el-tab-pane label="x-www-form-urlencoded" name="first">

        <el-switch v-model="reqFrom.isParam" @change="isNeedParam()" active-text="参数" inactive-text="无需参数"></el-switch>

        <el-card class="educationExperienceTable" v-if="reqFrom.isParam">
          <el-table :data="this.reqFrom.reqParam" stripe border>

            <el-table-column label="参数值" align="center" width="400px">
              <template slot-scope="scope">
                <div class="educationExperienceDiv">
                  <el-input v-model="scope.row.paramValue" placeholder="">
                  </el-input>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="136px">
              <template slot-scope="scope">
                <el-button type="success"
                           size="mini"
                           icon="el-icon-circle-plus-outline"
                           v-if="scope.row.show === 'true'"
                           plain
                           @click="pushNewParam(scope.$index)">
                </el-button>
                <el-button type="danger"
                           size="mini"
                           icon="el-icon-delete"
                           plain
                           @click="deleteParam(scope.$index)">
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

      </el-tab-pane>

      <el-tab-pane label="raw" name="second">
        <textarea rows="10" cols="120" style="font-size:16px;" v-model="reqFrom.jsonReqContext">
        </textarea>
      </el-tab-pane>

      <el-tab-pane label="xml" name="three">
        <textarea rows="10" cols="120" style="font-size:16px;" v-model="reqFrom.xmlReqContext"></textarea>
      </el-tab-pane>
    </el-tabs>

    <el-divider></el-divider>
    <el-tabs v-model="this.contextshow" v-if="this.isShowContext">
      <el-tab-pane label="响应内容" name="context">
        <code style="font-size:16px;height: 100px;width: 50px">
          {{resultContext}}
        </code>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  export default {
    name: "soapPage",
    data() {
      return {
        reqFrom: {
          xmlReqContext:"",
          reqStyle: "GET",
          mothodName: "",
          isScheduledTasks: false,
          activeName: 'first',
          reqUrl: "",
          startTime: '',
          endTime: '',
          jsonReqContext: "",
          isParam: false,
          reqParam: [{
            paramValue: '',
            show: 'true'
          }]
        },
        options: [{
          value: 'GET',
          label: 'GET'
        }, {
          value: 'POST',
          label: 'POST'
        }],

        isShowContext: false,
        contextshow: "context",
        resultContext: ""
      }
    },
    mounted: function () {
      let isRefresh = sessionStorage.getItem('isRefresh');
      console.log(isRefresh);
      if (isRefresh == 0) {
        sessionStorage.setItem('isRefresh', 1);
        console.log(sessionStorage.getItem('isRefresh'));
        this.$router.go(0);
      }
    },
    methods: {
      changeSoapReqStyle() {
        console.info(this.reqFrom.reqStyle);
        if ("POST" == this.reqFrom.reqStyle) {
          console.log("post")
          this.reqFrom.activeName = 'three';
        } else {
          console.log("get")
          this.reqFrom.activeName = "first";
        }
      },
      changeReqStyle(value) {
        if ("POST" == value) {
          this.activeName = 'second';
        } else {
          this.activeName = "first";
        }
      },
      reqTest() {
        console.log(this.reqFrom)

        this.axios.post('http://localhost:8080/test/reqSoap',
          {
            reqFrom: this.reqFrom
          }
        )
          .then((response) => {
            const result = response.data;
            if (result.code == '0') {
              console.log("失败")
              this.$message.error(result.msg);
            } else {
              this.resultContext = result.date;
              this.isShowContext = true;
              console.log("成功")
            }
          })
          .catch(function (error) {
            this.$message.error("失败");
            console.log(error);
          })
          .then(function () {
            // always executed
          });

      },
      pushNewParam(index) {
        const list = this.reqFrom.reqParam;
        list[index].show = 'false';
        list.push({
          paramValue: '',
          show: 'true',
        });
        this.reqFrom.reqParam = list;
      },
      deleteParam(index) {
        const list = this.reqFrom.reqParam;
        if (index === 0 && list.length === 1) {
          list.splice(index, 1);
          list.push({
            paramValue: '',
            show: 'true',
          });
        } else {
          list.splice(index, 1);
        }
        if (index === list.length) {
          list[index - 1].show = 'true';
        }
      },
      isNeedParam() {
        console.log("是否需要参数")
        this.reqFrom.reqParam = [];
        this.reqFrom.reqParam.push({
          paramValue: '',
          show: 'true',
        })
        console.log(this.reqFrom.isParam);
      }
    }
  }
</script>

<style scoped>
  .el-select .el-input {
    width: 130px;
  }

  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
