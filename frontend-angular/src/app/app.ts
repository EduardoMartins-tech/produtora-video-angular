import { Component, signal } from '@angular/core';
import { Clientes } from './clientes/clientes';
import { Projetos } from './projetos/projetos';
import { Entregas } from './entregas/entregas';

type Aba = 'clientes' | 'projetos' | 'entregas';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [Clientes, Projetos, Entregas],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  // Qual tela está aberta no menu
  protected readonly aba = signal<Aba>('clientes');

  abrir(aba: Aba): void {
    this.aba.set(aba);
  }
}
