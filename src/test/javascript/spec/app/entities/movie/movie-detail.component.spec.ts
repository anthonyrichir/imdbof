/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImdbofTestModule } from '../../../test.module';
import { MovieDetailComponent } from 'app/entities/movie/movie-detail.component';
import { Movie } from 'app/shared/model/movie.model';

describe('Component Tests', () => {
    describe('Movie Management Detail Component', () => {
        let comp: MovieDetailComponent;
        let fixture: ComponentFixture<MovieDetailComponent>;
        const route = ({ data: of({ movie: new Movie(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ImdbofTestModule],
                declarations: [MovieDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MovieDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MovieDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.movie).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
