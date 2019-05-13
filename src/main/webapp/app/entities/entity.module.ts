import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'category',
                loadChildren: './category/category.module#ImdbofCategoryModule'
            },
            {
                path: 'movie',
                loadChildren: './movie/movie.module#ImdbofMovieModule'
            },
            {
                path: 'category',
                loadChildren: './category/category.module#ImdbofCategoryModule'
            },
            {
                path: 'movie',
                loadChildren: './movie/movie.module#ImdbofMovieModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ImdbofEntityModule {}
