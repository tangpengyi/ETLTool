<template>

  <div>
    <el-container style="height: 580px; border: 1px solid #eee;">
      <el-header>
        <el-row class="tac">
          <el-col :span="12" style="font-size: 20px">
            ETL-Interface-Test
          </el-col>
          <el-col :span="12" style="text-align: right; font-size: 12px">
            <el-dropdown>
              <i class="el-icon-setting" style="margin-right: 15px"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <router-link to="/logout">退出登陆</router-link>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <span>admin</span>
          </el-col>
        </el-row>

      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-menu :default-openeds="['1', '3']">
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-s-unfold"></i>测试类型</template>
              <el-menu-item-group>
                <router-link to="/protocol/http">
                  <el-menu-item index="1-1">HTTP协议</el-menu-item>
                </router-link>
                <router-link to="/protocol/soap">
                  <el-menu-item index="1-2">Soap协议</el-menu-item>
                </router-link>
                <router-link to="/protocol/socket">
                  <el-menu-item index="1-3">Socket协议</el-menu-item>
                </router-link>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>

        </el-aside>


        <!--//主内容-->
        <el-main>
          <router-view />
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>

  export default {
    name: "index",
    data() {
      return {
        input: '',
        currentRole:'',
        item: {type: '', label: '标签一'}
      };
    },
    methods: {
      test() {
        console.log("test");
      }
    },
    mounted: function () {
      console.log("mune-mounted");
      this.test();
    },
    beforeRouteEnter:(to,from,next)=>{
      console.log("beforeRouteEnter");
      next(vm=>{
        vm.test();
      })
    }
  }
</script>

<style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }

  .router-link {
    text-decoration: none;
  }

  a {
    text-decoration: none;
  }
</style>
