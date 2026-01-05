package dominio;

public class Pet {
    private String nome;
    private int idade;
    private int peso;
    private TipoPet Tipopet;
    private String raca;
    private SexoPet sexoPet;
    private Endereco endereco;


    public Pet(String nome, int idade, int peso, TipoPet tipopet, SexoPet sexoPet, Endereco endereco,String raca) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        Tipopet = tipopet;
        this.sexoPet = sexoPet;
        this.endereco = endereco;
        this.raca = raca;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Pet() {

    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", peso=" + peso +
                '}';
    }


    public TipoPet getTipopet() {
        return Tipopet;
    }

    public void setTipopet(TipoPet tipopet) {
        Tipopet = tipopet;
    }

    public SexoPet getSexoPet() {
        return sexoPet;
    }

    public void setSexoPet(SexoPet sexoPet) {
        this.sexoPet = sexoPet;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
