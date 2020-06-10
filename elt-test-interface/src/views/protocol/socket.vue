<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/protocol/http' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>Socket协议</el-breadcrumb-item>
    </el-breadcrumb>
    <el-divider></el-divider>

    <el-form ref="reqFrom" :model="reqFrom" label-width="80px">
      <el-form-item label="请求地址:">
        <el-input placeholder="请求地址" v-model="reqFrom.reqUrl" style="width:50%" class="input-with-select"></el-input>
        <el-button type="primary" @click="connection()">连接</el-button>
        <el-button type="primary" @click="disconnect()">断开</el-button>
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

    <el-container>
      <el-aside width="420px">
        <span>发送消息：</span>
        <br/><br/>
        <textarea rows="8" cols="50" style="font-size:16px;" v-html="responscontext.replace(/\n|\r\n/g, '<br>') " v-model="reqFrom.sendMsg"></textarea><br/>
        <el-button type="primary" @click="sendMessage()" style="width: 420px">发送</el-button>
      </el-aside>
      <el-main>
        <span>消息窗口：</span>
        <br/><br/>
        <textarea rows="20" cols="80" style="font-size:16px;" v-model="responscontext"></textarea><br/>
      </el-main>
    </el-container>

  </div>
</template>

<script>
  export default {
    name: "socket",
    data() {
      return {
        reqFrom: {
          isScheduledTasks: false,
          startTime: '',
          endTime: '',
          reqUrl: "",
          saveUrl:"",
          sendMsg: ""
        },
        responscontext:""
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
      connection(){
        console.info("连接成功");
        this.reqFrom.saveUrl = this.reqFrom.reqUrl;
        this.axios.post('/api/test/socketConnection',
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
              this.responscontext = this.responscontext+"\n"+result.date;
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
      disconnect(){
        console.info("断开连接");
        this.axios.get('/api/test/disconnectSocket')
          .then((response) => {
            const result = response.data;
            if (result.code == '0') {
              console.log("失败")
              this.$message.error(result.msg);
            } else {
              this.responscontext = this.responscontext+"\n"+result.date;
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
      sendMessage(){
        console.info("发送消息");
        this.responscontext = this.responscontext+"\n客户端："+this.reqFrom.sendMsg;
        this.saveUrl = this.reqFrom.reqUrl;
        this.axios.post('/api/test/socketSendMsg',
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
              this.responscontext = this.responscontext+"\n服务端："+result.date;
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
      }
    }
  }
</script>

<style lang="scss">
  .el-select .el-input {
    width: 130px;
  }

  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
