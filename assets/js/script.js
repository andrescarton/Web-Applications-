function menuShow() {
    let menuMobile = document.querySelector('.mobile-menu');
    if (menuMobile.classList.contains('open')) {
        menuMobile.classList.remove('open');
        document.querySelector('.icon').src = "assets/img/menu_white_36dp.svg";
    } else {
        menuMobile.classList.add('open');
        document.querySelector('.icon').src = "assets/img/close_white_36dp.svg";
    }
}

// Obtém os elementos necessários
let currentIndex = 0; // Índice da imagem atual
const images = document.querySelectorAll('.carousel img'); // Seleciona todas as imagens do carrossel
const totalImages = images.length; // Total de imagens
const carousel = document.querySelector('.carousel'); // Seleciona o contêiner do carrossel

// Função para mover as imagens para a esquerda
function moveLeft() {
    if (currentIndex > 0) {
        currentIndex--;
    } else {
        currentIndex = totalImages - 1; // Volta para a última imagem
    }
    updateCarouselPosition();
}

// Função para mover as imagens para a direita
function moveRight() {
    if (currentIndex < totalImages - 1) {
        currentIndex++;
    } else {
        currentIndex = 0; // Volta para a primeira imagem
    }
    updateCarouselPosition();
}

// Função para atualizar a posição do carrossel
function updateCarouselPosition() {
    const newTransformValue = `translateX(-${currentIndex * 100}%)`; // Move a posição das imagens
    carousel.style.transform = newTransformValue;
}

// Adiciona eventos de clique nas setas
document.querySelector('.arrow.left').addEventListener('click', moveLeft);
document.querySelector('.arrow.right').addEventListener('click', moveRight);

function closeAndRedirect() {
    document.getElementById('register-modal').style.display = 'none'; // Fecha o modal
    window.location.href = 'index.html'; // Redireciona para a página inicial
}