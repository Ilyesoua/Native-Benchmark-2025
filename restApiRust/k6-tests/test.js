import http from 'k6/http';
import { check, group, sleep } from 'k6';

const BASE_URL = 'http://api-benchmark:8080/api';
const maxRetries = 60;
const delaySeconds = 20;


export const options = {
    setupTimeout: '1300s',
    stages: [
        { duration: '30s', target: 10 },
        { duration: '1m', target: 10 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        'http_req_duration{tag:health}': ['p(95)<500'],
        'http_req_duration{tag:create}': ['p(95)<500'],
        'http_req_duration{tag:get_by_id}': ['p(95)<500'],
        'http_req_duration{tag:search}': ['p(95)<500'],
        'http_req_duration{tag:random}': ['p(95)<500'],
        'http_req_duration{tag:internal}': ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
    },
};

export function setup() {
    for (let i = 0; i < maxRetries; i++) {
        const res = http.get(`${BASE_URL}/health`);
        if (res.status === 200 && res.body.includes('ok')) {
            console.log('✅ API is ready!');
            return;
        }
        console.log(`⏳ Waiting for API... (${i + 1}/${maxRetries})`);
        sleep(delaySeconds);
    }
    throw new Error('❌ API not ready after waiting.');
}

export default function () {
    group('Healthcheck', () => {
        let res = http.get(`${BASE_URL}/health`, {
            tags: { tag: 'health' }
        });
        check(res, {
            'health status is 200': (r) => r.status === 200,
            'health body is ok': (r) => r.body.includes('ok'),
        });
    });

    group('Create Movie', () => {
        let movieId = Math.floor(Math.random() * 1000) + 1;

        let payload = JSON.stringify({
            id: movieId,
            title: `TestMovie_${__VU}_${__ITER}`,
            director: "Director Name",
            release_date: "2024"
        });
        let headers = { 'Content-Type': 'application/json' };
        let res = http.post(`${BASE_URL}/movies`, payload, {
            headers: headers,
            tags: { tag: 'create' }
        });

        check(res, {
            'create status is 200': (r) => r.status === 200,
            'create returned movie': (r) => r.json('message') === "Movie saved successfully",
        });


        group('Get Movie by ID', () => {
            let resId = http.get(`${BASE_URL}/movies/${movieId}`, {
                tags: { tag: 'get_by_id' }
            });
            check(resId, {
                'get by id status is 200': (r) => r.status === 200,
                // 'get by id has correct title': (r) => r.json('Title').startsWith('TestMovie_'),
            });
        });
    });

    group('Search Movie by Title', () => {
        const searchTitle = 'Inception';
        let res = http.get(`${BASE_URL}/movies?title=${encodeURIComponent(searchTitle)}`, {
            tags: { tag: 'search' }
        });

        check(res, {
            'search status is 200': (r) => r.status === 200,
            'search returned movie': (r) => r.json('Actors') !== undefined,
        });
    });

    group('Random Movie', () => {
        let res = http.get(`${BASE_URL}/random`, {
            tags: { tag: 'random' }
        });
        check(res, {
            'random status is 200': (r) => r.status === 200,
            'random returned movie': (r) => r.json('id') !== undefined,
        });
    });

    group('Internal Process', () => {
        let res = http.get(`${BASE_URL}/internal/process`, {
            tags: { tag: 'internal' }
        });
        check(res, {
            'process status is 200': (r) => r.status === 200,
        });
    });

    sleep(1);
}
