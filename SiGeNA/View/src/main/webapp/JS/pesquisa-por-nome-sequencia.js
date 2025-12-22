let campoPesquisa = document.querySelector('.pesquisa input[type="text"]');
let campoSequencia = document.querySelector('.pesquisa .sequencia');
let isPesquisaRedirect = false;
const nav = performance.getEntriesByType("navigation")[0];

if (nav && nav.type === "reload") {
  localStorage.removeItem("campoPesquisa");
  localStorage.removeItem("campoSequencia");
  localStorage.removeItem("campoOrdem");

  let url = new URL(window.location.href);
  url.searchParams.delete("busca");
  url.searchParams.delete("sequencia");
  url.searchParams.delete("ordem");

  window.location.href = url.toString();
}

window.addEventListener('pagehide', function () {
  if (!isPesquisaRedirect) {
    localStorage.removeItem('campoPesquisa');
    localStorage.removeItem('campoSequencia');
    localStorage.removeItem('campoOrdem');
  }
});

if (campoPesquisa) {
  campoPesquisa.addEventListener('change', function () {
    let termoBusca = this.value.trim();
    let url = new URL(window.location.href);

    isPesquisaRedirect = true;

    if (termoBusca) {
      url.searchParams.set('busca', termoBusca);
      localStorage.setItem('campoPesquisa', termoBusca);
    } else {
      url.searchParams.delete('busca');
      localStorage.removeItem("campoPesquisa");
    }

    window.location.href = url.toString();
  });
}

if (campoSequencia) {
  campoSequencia.addEventListener('change', function () {
    let termoSequencia = this.value;
    let termoOrdem = this.options[this.selectedIndex]?.dataset.ordem;
    let url = new URL(window.location.href);

    isPesquisaRedirect = true;

    if (termoSequencia && termoOrdem) {
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
}

window.addEventListener('load', function () {
  let campoPesquisaValor = localStorage.getItem('campoPesquisa');
  let campoSequenciaValor = localStorage.getItem('campoSequencia');
  let campoOrdemValor = localStorage.getItem('campoOrdem');

  if (campoPesquisa && campoPesquisaValor !== null) {
    campoPesquisa.value = campoPesquisaValor;
  }

  if (campoSequencia && campoSequenciaValor && campoOrdemValor) {
    for (let opt of campoSequencia.options) {
      if (opt.value === campoSequenciaValor && opt.dataset.ordem === campoOrdemValor) {
        opt.selected = true;
        break;
      }
    }
  }
});
