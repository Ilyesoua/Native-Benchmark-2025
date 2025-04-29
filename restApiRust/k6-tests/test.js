import http from 'k6/http';
import { Trend } from 'k6/metrics';
import { check, group, sleep } from 'k6';

export const healthTrend = new Trend('http_req_duration_health');
export const createTrend = new Trend('http_req_duration_create');
export const getByIdTrend = new Trend('http_req_duration_get_by_id');
export const searchTrend = new Trend('http_req_duration_search');
export const randomTrend = new Trend('http_req_duration_random');
export const internalTrend = new Trend('http_req_duration_internal');

const BASE_URL = 'http://api-benchmark:8080/api';
const maxRetries = 60;
const delaySeconds = 20;

export const options = {
    setupTimeout: '1300s',
    vus: 10, // 1 utilisateur virtuel
    iterations: 1000, // 10000 exécutions
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
            timeout: '10s', tags: { tag: 'health' }
        });        
        
        check(res, {
            'health status is 200': (r) => r.status === 200,
            'health body is ok': (r) => r.body.includes('ok'),
        });

        if (res.status === 200) {
            healthTrend.add(res.timings.duration);
        }
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
            timeout: '10s', tags: { tag: 'create' }
        });
        
        check(res, {
            'create status is 200': (r) => r.status === 200,
            'create returned movie': (r) => r.json('message') === "Movie saved successfully",
        });

        if (res.status === 200) {
            createTrend.add(res.timings.duration);
        }

        group('Get Movie by ID', () => {
            let resId = http.get(`${BASE_URL}/movies/${movieId}`, {
                timeout: '10s', tags: { tag: 'get_by_id' }
            });
            check(resId, {
                'get by id status is 200': (r) => r.status === 200,
                // 'get by id has correct title': (r) => r.json('Title').startsWith('TestMovie_'),
            });

            if (res.status === 200) {
                getByIdTrend.add(resId.timings.duration);
            }
        });
    });

    group('Search Movie by Title', () => {
        const searchTitle = 'Inception';
        let res = http.get(`${BASE_URL}/movies?title=${encodeURIComponent(searchTitle)}`, {
            timeout: '10s', tags: { tag: 'search' }
        });
        
        check(res, {
            'search status is 200': (r) => r.status === 200,
            'search returned movie': (r) => r.json('Actors') !== undefined,
        });

        if (res.status === 200) {
            searchTrend.add(res.timings.duration);
        }
    });

    group('Random Movie', () => {
        let res = http.get(`${BASE_URL}/random`, {
            timeout: '10s', tags: { tag: 'random' }
        });
        
        check(res, {
            'random status is 200': (r) => r.status === 200,
            'random returned movie': (r) => r.json('id') !== undefined,
        });

        if (res.status === 200) {
            randomTrend.add(res.timings.duration);
        }
    });

    group('Internal Process', () => {
        let res = http.get(`${BASE_URL}/internal/process`, {
            timeout: '10s', tags: { tag: 'internal' }
        });        
        
        check(res, {
            'process status is 200': (r) => r.status === 200,
        });

        if (res.status === 200) {
            internalTrend.add(res.timings.duration);
        }
    });

    sleep(1);
}
