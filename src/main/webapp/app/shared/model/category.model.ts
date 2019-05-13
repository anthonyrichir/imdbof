import { IMovie } from 'app/shared/model/movie.model';

export interface ICategory {
    id?: number;
    name?: string;
    movies?: IMovie[];
}

export class Category implements ICategory {
    constructor(public id?: number, public name?: string, public movies?: IMovie[]) {}
}
