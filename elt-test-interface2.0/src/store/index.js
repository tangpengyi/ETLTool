import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const state = {
  variable: {
    isRefresh: 0
  }
};

const getters = {
  getVariable(state) {
    return state.variable;
  }
};

const mutations = {
  updateUser(state, variable) {
    state.user = variable;
  }
};

const actions = {
  asyncUpdateVariable(context, variable) {
    context.commit('updateUser', variable);
  }
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
});
