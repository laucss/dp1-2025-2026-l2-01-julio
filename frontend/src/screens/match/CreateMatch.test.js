import { render, screen, fireEvent, waitFor } from '../test-utils';
import CreateLobby from './CreateLobby';

// Mock de useNavigate
const mockedNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedNavigate,
}));

// Mock de tokenService
jest.mock('../../services/token.service', () => ({
  getLocalAccessToken: () => 'fake-jwt-token',
}));

// Mock de fetch
global.fetch = jest.fn();

describe("CreateLobby component tests", () => {

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("renders form elements correctly", async () => {
    render(<CreateLobby />);

    expect(screen.getByLabelText(/Nombre/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Número de jugadores/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Número de NPCs/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Partida privada/i)).toBeInTheDocument();
    expect(screen.getByText(/Crear/i)).toBeInTheDocument();
    expect(screen.getByText(/Cancelar/i)).toBeInTheDocument();
  });

  test("increments and decrements player and NPC counts", async () => {
    render(<CreateLobby />);

    const playerCount = screen.getByText("4");
    const npcCount = screen.getByText("3");

    const decreasePlayerBtn = screen.getByText("↓");
    const increasePlayerBtn = screen.getByText("↑");
    const decreaseNpcBtn = screen.getByText("-");
    const increaseNpcBtn = screen.getByText("+");

    fireEvent.click(increasePlayerBtn);
    expect(playerCount.textContent).toBe("5");

    fireEvent.click(decreasePlayerBtn);
    fireEvent.click(decreasePlayerBtn);
    expect(playerCount.textContent).toBe("3"); // min 3

    fireEvent.click(increaseNpcBtn);
    expect(npcCount.textContent).toBe("4");

    fireEvent.click(decreaseNpcBtn);
    fireEvent.click(decreaseNpcBtn);
    expect(npcCount.textContent).toBe("3"); // min 3
  });

  test("toggles private checkbox",async () => {
    render(<CreateLobby />);
    const checkbox = screen.getByLabelText(/Partida privada/i);
    expect(checkbox.checked).toBe(false);
    fireEvent.click(checkbox);
    expect(checkbox.checked).toBe(true);
  });

  test("submits form and navigates on success", async () => {
    const fakeLobby = { id: 123 };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeLobby,
    });

    render(<CreateLobby />);

    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: "Test Lobby" } });
    fireEvent.click(screen.getByText("Crear"));

    // Espera a que fetch se haya llamado
    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith("/api/v1/matches/lobbies", expect.any(Object));
    });

    // Espera a que navigate se haya llamado
    await waitFor(() => {
      expect(mockedNavigate).toHaveBeenCalledWith("/lobby/123");
    });
  });

  test("shows alert on fetch error", async () => {
    global.alert = jest.fn();
    fetch.mockResolvedValueOnce({ ok: false, status: 500 });

    render(<CreateLobby />);
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: "Test Lobby" } });
    fireEvent.click(screen.getByText("Crear"));

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith("No se pudo crear la partida");
    });
  });

  test("cancel button navigates back", async() => {
    render(<CreateLobby />);
    fireEvent.click(screen.getByText("Cancelar"));
    expect(mockedNavigate).toHaveBeenCalledWith("/");
  });

  test("back arrow navigates home", async() => {
    render(<CreateLobby />);
    fireEvent.click(screen.getByText("￩"));
    expect(mockedNavigate).toHaveBeenCalledWith("/");
  });

});
