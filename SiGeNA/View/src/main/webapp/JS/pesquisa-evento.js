let campoPesquisa = document.querySelector('.pesquisa input[type="text"]');
let campoFiltro = document.querySelector('.pesquisa .filtro');
let campoSequencia = document.querySelector('.pesquisa .sequencia');
let isPesquisaRedirect = false;
const nav = performance.getEntriesByType("navigation")[0];

if(nav && nav.type === "reload") {
  localStorage.removeItem("campoPesquisa");
  localStorage.removeItem("campoFiltro");
  localStorage.removeItem("campoSequencia");

  let url = new URL(window.location.href);

  url.searchParams.delete("busca");
  url.searchParams.delete("filtro");
  url.searchParams.delete("sequencia");

  window.location.href = url.toString();
}

window.addEventListener('pagehide', function() {
  if(!isPesquisaRedirect) {
    localStorage.removeItem('campoPesquisa');
    localStorage.removeItem('campoFiltro');
    localStorage.removeItem("campoSequencia");
  } 
});

campoPesquisa.addEventListener('change', function() {
  let termoBusca = this.value.trim();
  let url = new URL(window.location.href);

  if(termoBusca) {
    isPesquisaRedirect = true;
    url.searchParams.set('busca', termoBusca);
    localStorage.setItem('campoPesquisa', termoBusca);
  } else {
    isPesquisaRedirect = true; 
    url.searchParams.delete('busca');
    localStorage.removeItem("campoPesquisa");
  }

  window.location.href = url.toString();
});

campoFiltro.addEventListener('change', function() {
  let termoFiltro = this.value.trim();
  let url = new URL(window.location.href);
  
  isPesquisaRedirect = true;
  
  if (!termoFiltro) {
    url.searchParams.delete("filtro");
    localStorage.removeItem("campoFiltro");
  } else {
    url.searchParams.set("filtro", termoFiltro);
    localStorage.setItem("campoFiltro", termoFiltro);
  }

  window.location.href = url.toString();
});

campoSequencia.addEventListener('change', function() {
  let termoSequencia = this.value.trim();
  let termoOrdem = this.options[this.selectedIndex].dataset.ordem;
  let url = new URL(window.location.href);

  if(termoSequencia) {
    isPesquisaRedirect = true;
    url.searchParams.set('sequencia', termoSequencia);
    url.searchParams.set('ordem', termoOrdem);
    localStorage.setItem('campoSequencia', termoSequencia);
    localStorage.setItem('campoOrdem', termoOrdem);
  } else {
    url.searchParams.delete('sequencia');
    url.searchParams.delete('ordem');
    localStorage.removeItem('campoSequencia');
    localStorage.removeItem('campoOrdem');
  }

  window.location.href = url.toString();
});

window.addEventListener('load', function() {
  let campoPesquisaValor = localStorage.getItem('campoPesquisa');
  let campoFiltroValor = localStorage.getItem('campoFiltro');
  let campoSequenciaValor = this.localStorage.getItem('campoSequencia');
  let campoOrdemValor = localStorage.getItem('campoOrdem');
  
  if(campoPesquisaValor !== null)
    campoPesquisa.value = campoPesquisaValor;

  let url = new URL(window.location.href);

if (campoFiltroValor !== null && url.searchParams.has("filtro")) {
  campoFiltro.value = campoFiltroValor;
}

  if (campoSequenciaValor !== null && campoOrdemValor !== null) {
    for (let opt of campoSequencia.options) {
        if (opt.value === campoSequenciaValor && opt.dataset.ordem === campoOrdemValor) {
            campoSequencia.value = opt.value;
            opt.selected = true;
            break;
        }
    }
}
});