// Define the canvas and context
const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

// Glove properties
const gloveSize = 100;
const gloveSpeed = 4; // Punching speed
const glovePadding = 50; // Glove position offset

let isPunching = false;
let glovePositionLeft = { x: glovePadding, y: canvas.height / 2 - gloveSize / 2 };
let glovePositionRight = { x: canvas.width - glovePadding - gloveSize, y: canvas.height / 2 - gloveSize / 2 };

// Target (Mr. Wong) properties
const target = {
    x: canvas.width / 2 - 50,
    y: canvas.height / 2 - 50,
    width: 100,
    height: 100,
    health: 100
};

// Background elements properties
let score = 0;
let knockOutFlag = false;

// Utility functions
function drawGlove(x, y) {
    ctx.beginPath();
    ctx.arc(x, y, gloveSize / 2, 0, Math.PI * 2);
    ctx.fillStyle = '#FF5733'; // Glove color
    ctx.fill();
    ctx.stroke();
}

function drawTarget() {
    ctx.beginPath();
    ctx.arc(target.x + target.width / 2, target.y + target.height / 2, target.width / 2, 0, Math.PI * 2);
    ctx.fillStyle = '#F1C27D'; // Skin tone color
    ctx.fill();
    ctx.stroke();

    // Eyes
    ctx.beginPath();
    ctx.arc(target.x + target.width / 3, target.y + target.height / 3, 10, 0, Math.PI * 2); // Left eye
    ctx.arc(target.x + 2 * target.width / 3, target.y + target.height / 3, 10, 0, Math.PI * 2); // Right eye
    ctx.fillStyle = '#000'; // Eye color
    ctx.fill();

    // Mouth
    ctx.beginPath();
    ctx.arc(target.x + target.width / 2, target.y + 2 * target.height / 3, 20, 0, Math.PI);
    ctx.lineWidth = 5;
    ctx.stroke();
}

function drawHealthBar() {
    ctx.fillStyle = '#d9534f'; // Background of the health bar
    ctx.fillRect(10, 10, 200, 30);
    ctx.fillStyle = '#5bc0de'; // Health color
    ctx.fillRect(10, 10, (200 * target.health) / 100, 30);
}

function drawScore() {
    ctx.font = '25px Arial';
    ctx.fillStyle = '#333';
    ctx.fillText(`Score: ${score}`, canvas.width - 120, 40);
}

function drawBackground() {
    // Draw classroom walls
    ctx.fillStyle = '#4e92a3';
    ctx.fillRect(60, 40, 120, 80);
    ctx.fillRect(canvas.width - 180, 40, 120, 80);

    // Draw floor
    ctx.fillStyle = '#deb887';
    ctx.fillRect(0, canvas.height - 50, canvas.width, 50);

    // Draw blackboard
    ctx.fillStyle = '#34495e';
    ctx.fillRect(100, 20, 400, 50);

    // Draw desks and chairs
    drawDesk(150, 230);
    drawDesk(230, 230);
    drawDesk(310, 230);
    drawDesk(390, 230);
    drawDesk(470, 230);

    drawChair(150, 270);
    drawChair(230, 270);
    drawChair(310, 270);
    drawChair(390, 270);
    drawChair(470, 270);
}

function drawDesk(x, y) {
    ctx.fillStyle = '#8b4513';
    ctx.fillRect(x, y, 80, 40);
}

function drawChair(x, y) {
    ctx.fillStyle = '#a9a9a9';
    ctx.beginPath();
    ctx.arc(x + 40, y + 40, 20, 0, Math.PI * 2);
    ctx.fill();
}

// Punching logic
function punch() {
    if (!isPunching && target.health > 0) {
        isPunching = true;

        // Check for collision with the target (Mr. Wong)
        if (Math.abs(glovePositionLeft.x - (target.x + target.width / 2)) < gloveSize / 2 && 
            Math.abs(glovePositionLeft.y - (target.y + target.height / 2)) < gloveSize / 2) {
            target.health -= 10;
            score += 10;
            if (target.health <= 0) {
                knockOut();
            }
        }
        setTimeout(() => {
            isPunching = false;
        }, 200); // Reset punching state
    }
}

// Knockout effect
function knockOut() {
    knockOutFlag = true;
    setTimeout(() => {
        alert('Mr. Wong is knocked out!');
        resetGame();
    }, 1000);
}

// Reset the game after knockout
function resetGame() {
    target.health = 100;
    score = 0;
    knockOutFlag = false;
}

// Game loop
function gameLoop() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    drawBackground();
    drawGlove(glovePositionLeft.x, glovePositionLeft.y);
    drawGlove(glovePositionRight.x, glovePositionRight.y);
    drawTarget();
    drawHealthBar();
    drawScore();

    if (isPunching) {
        glovePositionLeft.x += gloveSpeed;
        glovePositionRight.x -= gloveSpeed;

        if (glovePositionLeft.x > canvas.width || glovePositionRight.x < 0) {
            glovePositionLeft.x = glovePadding;
            glovePositionRight.x = canvas.width - glovePadding - gloveSize;
        }
    }

    if (knockOutFlag) {
        ctx.save();
        ctx.translate(target.x + target.width / 2, target.y + target.height / 2);
        ctx.rotate(Math.random() * 2 * Math.PI);
        ctx.restore();
    }

    requestAnimationFrame(gameLoop);
}

// Set up event listeners
canvas.addEventListener('click', punch);

// Start the game loop
gameLoop();
