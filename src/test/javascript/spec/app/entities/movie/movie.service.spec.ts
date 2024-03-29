/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { MovieService } from 'app/entities/movie/movie.service';
import { IMovie, Movie } from 'app/shared/model/movie.model';

describe('Service Tests', () => {
    describe('Movie Service', () => {
        let injector: TestBed;
        let service: MovieService;
        let httpMock: HttpTestingController;
        let elemDefault: IMovie;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(MovieService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Movie(0, 'AAAAAAA', 'AAAAAAA', 0, 0, false, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, 'AAAAAAA', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Movie', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Movie(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Movie', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        originalTitle: 'BBBBBB',
                        voteCount: 1,
                        voteAverage: 1,
                        video: true,
                        popularity: 1,
                        posterPath: 'BBBBBB',
                        originalLanguage: 'BBBBBB',
                        backdropPath: 'BBBBBB',
                        adult: true,
                        releaseDate: 'BBBBBB',
                        overview: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Movie', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        originalTitle: 'BBBBBB',
                        voteCount: 1,
                        voteAverage: 1,
                        video: true,
                        popularity: 1,
                        posterPath: 'BBBBBB',
                        originalLanguage: 'BBBBBB',
                        backdropPath: 'BBBBBB',
                        adult: true,
                        releaseDate: 'BBBBBB',
                        overview: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Movie', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
