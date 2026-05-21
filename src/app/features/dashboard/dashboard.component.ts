import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { SideMenuComponent } from '../../shared/components/side-menu/side-menu.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { RotinaCardComponent } from '../../shared/components/rotina-card/rotina-card.component';
import { AcessosRecentesComponent } from '../../shared/components/acessos-recentes/acessos-recentes.component';
import { FavoritosAnotacoesComponent } from '../../shared/components/favoritos-anotacoes/favoritos-anotacoes.component';
import { AdicionarRotinaModalComponent } from './components/adicionar-rotina-modal/adicionar-rotina-modal.component';
import { RotinaService } from '../../core/services/rotina.service';
import { AuthService } from '../../core/auth/auth.service';
import { PortalService, AcessoRecenteBackend, AnotacaoBackend, FavoritoBackend } from '../../core/services/portal.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    SideMenuComponent,
    FooterComponent,
    RotinaCardComponent,
    AcessosRecentesComponent,
    FavoritosAnotacoesComponent,
    AdicionarRotinaModalComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private rotinaService = inject(RotinaService);
  private authService = inject(AuthService);
  private portalService = inject(PortalService);

  rotinas = this.rotinaService.getRotinasSignal();

  acessosRecentes = signal<AcessoRecenteBackend[]>([]);
  favoritos = signal<FavoritoBackend[]>([]);
  anotacoes = signal<AnotacaoBackend[]>([]);

  showModal = false;
  loading = false;
  error = '';

  ngOnInit() {
    this.carregarPortal();
  }

  carregarPortal() {
    const usuarioId = this.authService.usuarioId;

    if (!usuarioId) {
      this.error = 'Usuário não encontrado na sessão.';
      return;
    }

    this.loading = true;
    this.error = '';

    this.portalService.carregarPortalInicial(usuarioId).subscribe({
      next: (portal) => {
        this.acessosRecentes.set(portal.acessosRecentes || []);
        this.favoritos.set(portal.favoritos || []);
        this.anotacoes.set(portal.anotacoes || []);

        const servicos = portal.servicosDestaque?.length ? portal.servicosDestaque : portal.servicos;
        this.rotinaService.carregarComServicos(usuarioId, servicos || []).subscribe({
          next: () => {
            this.loading = false;
          },
          error: (err) => {
            this.rotinaService.setRotinasFromBackend(servicos || []);
            this.loading = false;
            console.error(err);
          }
        });
      },
      error: (err) => {
        this.error = err?.error?.mensagem || 'Não foi possível carregar os dados do portal.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  openModal() {
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }
}
