const vm = new Vue({
  el: '#app',
  data: {
    isLogged:true,
    login:null
  },
  mounted () {
    this.isLogged = true;
    this.login = localStorage.getItem('RegisteredStationLogin');
    localStorage.removeItem('RegisteredStationLogin');
  },
  methods: {
    loginStation(log,pass){
      axios.post('http://localhost:8081/sapi/login',{login:log,password:pass}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          this.isLogged=true;
          localStorage.setItem('StationToken', response.data.message);
          window.location.href = 'stationIndex.html';
        }
        else{
          this.isLogged=false;

        }
      //  store.commit('LOGIN_USER')
      }).catch( error => { console.log(error); });
    },
    registerStation(){
      window.location.href = 'stationRegister.html';
    }

  }
});
