# UnikornTube

##Convention de nommage:

Variable d'instance =>   _variable

Param�tre de fonction => parVariable

Variable locale => locVariable

###Example:

public class Exemple {
		//	Exemple de variable d'instance :: pr�fixe '_'
		private int _maVariable;

		/**
		*	Gets something
		*/
		public int GetSomething () {
			return _maVariable;
		}

		/**
		*	Sets Something : Exemple de param�tre :: pr�fixe 'par'
		*/
		public HRESULT SetSomething ( int parVariable ) {
			if ( 0 <= parVariable ) {
				int locExemple = 0;							//	Exemple de variable locale :: pr�fixe loc
				_maVariable = parVariable;
			}
		}
}