import http from 'k6/http';
import { check, group, sleep } from 'k6';

// ─── Options ──────────────────────────────────────────────────────────────────
export const options = {
    stages: [
        { duration: '10s', target: 10 }, // ramp-up
        { duration: '20s', target: 10 }, // hold
        { duration: '5s', target: 0 }, // ramp-down
    ],
    thresholds: {
        'http_req_duration': ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
    },
};

// ─── Config ───────────────────────────────────────────────────────────────────
const BASE_URL = 'http://localhost:8080';
const TOKEN = __ENV.API_TOKEN || 'test-token';

const PARAMS = {
    headers: {
        'Authorization': `Bearer ${TOKEN}`,
        'Content-Type': 'application/json',
    },
};

// ─── Helpers ──────────────────────────────────────────────────────────────────
function randomEmail() {
    return `user_${Math.floor(Math.random() * 1000000)}@example.com`;
}

function randomProduct() {
    const names = ['Widget Pro', 'Gadget Ultra', 'Tool Plus', 'Device Max', 'Item X'];
    const categories = ['electronics', 'furniture', 'accessories'];
    return {
        name: names[Math.floor(Math.random() * names.length)] + ` ${Math.floor(Math.random() * 999)}`,
        description: 'Auto-generated test product',
        price: parseFloat((Math.random() * 500 + 10).toFixed(2)),
        stock: Math.floor(Math.random() * 200) + 1,
        category: categories[Math.floor(Math.random() * categories.length)],
    };
}

// ─── Default function ─────────────────────────────────────────────────────────
export default function () {

    // ── Health checks ─────────────────────────────────────────────────────────
    group('Health endpoints', () => {
        const root = http.get(`${BASE_URL}/`, PARAMS);
        check(root, {
            'GET / → 200': (r) => r.status === 200,
            'GET / response < 200ms': (r) => r.timings.duration < 200,
        });

        const health = http.get(`${BASE_URL}/health`, PARAMS);
        check(health, {
            'GET /health → 200': (r) => r.status === 200,
            'GET /health response < 200ms': (r) => r.timings.duration < 200,
        });

        const actuator = http.get(`${BASE_URL}/actuator/health`, PARAMS);
        check(actuator, {
            'GET /actuator/health → 200': (r) => r.status === 200,
            'GET /actuator/health response < 300ms': (r) => r.timings.duration < 300,
        });
    });

    // ── Products ──────────────────────────────────────────────────────────────
    group('Products API', () => {
        // List all products
        const list = http.get(`${BASE_URL}/api/products`, PARAMS);
        check(list, {
            'GET /api/products → 200': (r) => r.status === 200,
            'GET /api/products returns array': (r) => Array.isArray(JSON.parse(r.body)),
            'GET /api/products response < 300ms': (r) => r.timings.duration < 300,
        });

        // Get product by ID (seeded IDs are 1-8)
        const id = Math.floor(Math.random() * 8) + 1;
        const get = http.get(`${BASE_URL}/api/products/${id}`, PARAMS);
        check(get, {
            'GET /api/products/{id} → 200': (r) => r.status === 200,
            'GET /api/products/{id} response < 300ms': (r) => r.timings.duration < 300,
        });

        // Filter by category
        const cats = ['electronics', 'furniture', 'accessories'];
        const cat = cats[Math.floor(Math.random() * cats.length)];
        const byCategory = http.get(`${BASE_URL}/api/products?category=${cat}`, PARAMS);
        check(byCategory, {
            'GET /api/products?category → 200': (r) => r.status === 200,
        });

        // Create a product
        const createRes = http.post(
            `${BASE_URL}/api/products`,
            JSON.stringify(randomProduct()),
            PARAMS
        );
        check(createRes, {
            'POST /api/products → 201': (r) => r.status === 201,
            'POST /api/products response < 400ms': (r) => r.timings.duration < 400,
        });
    });

    // ── Users ─────────────────────────────────────────────────────────────────
    group('Users API', () => {
        // List all users
        const list = http.get(`${BASE_URL}/api/users`, PARAMS);
        check(list, {
            'GET /api/users → 200': (r) => r.status === 200,
            'GET /api/users returns array': (r) => Array.isArray(JSON.parse(r.body)),
            'GET /api/users response < 300ms': (r) => r.timings.duration < 300,
        });

        // Get user by ID (seeded IDs are 1-5)
        const uid = Math.floor(Math.random() * 5) + 1;
        const getUser = http.get(`${BASE_URL}/api/users/${uid}`, PARAMS);
        check(getUser, {
            'GET /api/users/{id} → 200': (r) => r.status === 200,
        });

        // Create a new user (random email to avoid 409 conflicts)
        const newUser = {
            name: 'Perf Test User',
            email: randomEmail(),
            phone: '+1-555-9999',
            role: 'user',
        };
        const createUser = http.post(
            `${BASE_URL}/api/users`,
            JSON.stringify(newUser),
            PARAMS
        );
        check(createUser, {
            'POST /api/users → 201': (r) => r.status === 201,
            'POST /api/users response < 400ms': (r) => r.timings.duration < 400,
        });
    });

    sleep(1);
}
