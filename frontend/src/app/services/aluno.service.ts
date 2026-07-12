import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Aluno, CriarAlunoRequest } from '../models/aluno.model';

@Injectable({
  providedIn: 'root'
})
export class AlunoService {

  private readonly apiUrl = '/api/alunos';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Aluno[]> {
    return this.http.get<Aluno[]>(this.apiUrl);
  }

  criar(request: CriarAlunoRequest): Observable<Aluno> {
    return this.http.post<Aluno>(this.apiUrl, request);
  }
}